import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPegawai } from 'app/entities/pegawai/pegawai.model';
import { PegawaiService } from 'app/entities/pegawai/service/pegawai.service';
import { ICuti } from '../cuti.model';
import { CutiService } from '../service/cuti.service';

import { CutiFormService } from './cuti-form.service';
import { CutiUpdate } from './cuti-update';

describe('Cuti Management Update Component', () => {
  let comp: CutiUpdate;
  let fixture: ComponentFixture<CutiUpdate>;
  let activatedRoute: ActivatedRoute;
  let cutiFormService: CutiFormService;
  let cutiService: CutiService;
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

    fixture = TestBed.createComponent(CutiUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cutiFormService = TestBed.inject(CutiFormService);
    cutiService = TestBed.inject(CutiService);
    pegawaiService = TestBed.inject(PegawaiService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Pegawai query and add missing value', () => {
      const cuti: ICuti = { id: 14574 };
      const pegawai: IPegawai = { id: 28349 };
      cuti.pegawai = pegawai;

      const pegawaiCollection: IPegawai[] = [{ id: 28349 }];
      vitest.spyOn(pegawaiService, 'query').mockReturnValue(of(new HttpResponse({ body: pegawaiCollection })));
      const additionalPegawais = [pegawai];
      const expectedCollection: IPegawai[] = [...additionalPegawais, ...pegawaiCollection];
      vitest.spyOn(pegawaiService, 'addPegawaiToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cuti });
      comp.ngOnInit();

      expect(pegawaiService.query).toHaveBeenCalled();
      expect(pegawaiService.addPegawaiToCollectionIfMissing).toHaveBeenCalledWith(
        pegawaiCollection,
        ...additionalPegawais.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.pegawaisSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const cuti: ICuti = { id: 14574 };
      const pegawai: IPegawai = { id: 28349 };
      cuti.pegawai = pegawai;

      activatedRoute.data = of({ cuti });
      comp.ngOnInit();

      expect(comp.pegawaisSharedCollection()).toContainEqual(pegawai);
      expect(comp.cuti).toEqual(cuti);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICuti>();
      const cuti = { id: 2717 };
      vitest.spyOn(cutiFormService, 'getCuti').mockReturnValue(cuti);
      vitest.spyOn(cutiService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuti });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cuti);
      saveSubject.complete();

      // THEN
      expect(cutiFormService.getCuti).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cutiService.update).toHaveBeenCalledWith(expect.objectContaining(cuti));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICuti>();
      const cuti = { id: 2717 };
      vitest.spyOn(cutiFormService, 'getCuti').mockReturnValue({ id: null });
      vitest.spyOn(cutiService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuti: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cuti);
      saveSubject.complete();

      // THEN
      expect(cutiFormService.getCuti).toHaveBeenCalled();
      expect(cutiService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICuti>();
      const cuti = { id: 2717 };
      vitest.spyOn(cutiService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuti });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cutiService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePegawai', () => {
      it('should forward to pegawaiService', () => {
        const entity = { id: 28349 };
        const entity2 = { id: 15805 };
        vitest.spyOn(pegawaiService, 'comparePegawai');
        comp.comparePegawai(entity, entity2);
        expect(pegawaiService.comparePegawai).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
