import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPegawai } from '../pegawai.model';
import { PegawaiService } from '../service/pegawai.service';

import { PegawaiFormService } from './pegawai-form.service';
import { PegawaiUpdate } from './pegawai-update';

describe('Pegawai Management Update Component', () => {
  let comp: PegawaiUpdate;
  let fixture: ComponentFixture<PegawaiUpdate>;
  let activatedRoute: ActivatedRoute;
  let pegawaiFormService: PegawaiFormService;
  let pegawaiService: PegawaiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(PegawaiUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pegawaiFormService = TestBed.inject(PegawaiFormService);
    pegawaiService = TestBed.inject(PegawaiService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const pegawai: IPegawai = { id: 15805 };

      activatedRoute.data = of({ pegawai });
      comp.ngOnInit();

      expect(comp.pegawai).toEqual(pegawai);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPegawai>();
      const pegawai = { id: 28349 };
      vitest.spyOn(pegawaiFormService, 'getPegawai').mockReturnValue(pegawai);
      vitest.spyOn(pegawaiService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pegawai });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(pegawai);
      saveSubject.complete();

      // THEN
      expect(pegawaiFormService.getPegawai).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pegawaiService.update).toHaveBeenCalledWith(expect.objectContaining(pegawai));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPegawai>();
      const pegawai = { id: 28349 };
      vitest.spyOn(pegawaiFormService, 'getPegawai').mockReturnValue({ id: null });
      vitest.spyOn(pegawaiService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pegawai: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(pegawai);
      saveSubject.complete();

      // THEN
      expect(pegawaiFormService.getPegawai).toHaveBeenCalled();
      expect(pegawaiService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPegawai>();
      const pegawai = { id: 28349 };
      vitest.spyOn(pegawaiService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pegawai });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pegawaiService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
