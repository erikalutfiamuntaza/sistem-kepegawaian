import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPegawai } from 'app/entities/pegawai/pegawai.model';
import { PegawaiService } from 'app/entities/pegawai/service/pegawai.service';
import { IPenggajian } from '../penggajian.model';
import { PenggajianService } from '../service/penggajian.service';

import { PenggajianFormService } from './penggajian-form.service';
import { PenggajianUpdate } from './penggajian-update';

describe('Penggajian Management Update Component', () => {
  let comp: PenggajianUpdate;
  let fixture: ComponentFixture<PenggajianUpdate>;
  let activatedRoute: ActivatedRoute;
  let penggajianFormService: PenggajianFormService;
  let penggajianService: PenggajianService;
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

    fixture = TestBed.createComponent(PenggajianUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    penggajianFormService = TestBed.inject(PenggajianFormService);
    penggajianService = TestBed.inject(PenggajianService);
    pegawaiService = TestBed.inject(PegawaiService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Pegawai query and add missing value', () => {
      const penggajian: IPenggajian = { id: 16458 };
      const pegawai: IPegawai = { id: 28349 };
      penggajian.pegawai = pegawai;

      const pegawaiCollection: IPegawai[] = [{ id: 28349 }];
      vitest.spyOn(pegawaiService, 'query').mockReturnValue(of(new HttpResponse({ body: pegawaiCollection })));
      const additionalPegawais = [pegawai];
      const expectedCollection: IPegawai[] = [...additionalPegawais, ...pegawaiCollection];
      vitest.spyOn(pegawaiService, 'addPegawaiToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ penggajian });
      comp.ngOnInit();

      expect(pegawaiService.query).toHaveBeenCalled();
      expect(pegawaiService.addPegawaiToCollectionIfMissing).toHaveBeenCalledWith(
        pegawaiCollection,
        ...additionalPegawais.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.pegawaisSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const penggajian: IPenggajian = { id: 16458 };
      const pegawai: IPegawai = { id: 28349 };
      penggajian.pegawai = pegawai;

      activatedRoute.data = of({ penggajian });
      comp.ngOnInit();

      expect(comp.pegawaisSharedCollection()).toContainEqual(pegawai);
      expect(comp.penggajian).toEqual(penggajian);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPenggajian>();
      const penggajian = { id: 20594 };
      vitest.spyOn(penggajianFormService, 'getPenggajian').mockReturnValue(penggajian);
      vitest.spyOn(penggajianService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ penggajian });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(penggajian);
      saveSubject.complete();

      // THEN
      expect(penggajianFormService.getPenggajian).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(penggajianService.update).toHaveBeenCalledWith(expect.objectContaining(penggajian));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPenggajian>();
      const penggajian = { id: 20594 };
      vitest.spyOn(penggajianFormService, 'getPenggajian').mockReturnValue({ id: null });
      vitest.spyOn(penggajianService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ penggajian: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(penggajian);
      saveSubject.complete();

      // THEN
      expect(penggajianFormService.getPenggajian).toHaveBeenCalled();
      expect(penggajianService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPenggajian>();
      const penggajian = { id: 20594 };
      vitest.spyOn(penggajianService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ penggajian });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(penggajianService.update).toHaveBeenCalled();
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
