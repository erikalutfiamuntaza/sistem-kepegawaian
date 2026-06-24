import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPegawai } from 'app/entities/pegawai/pegawai.model';
import { PegawaiService } from 'app/entities/pegawai/service/pegawai.service';
import { IAbsensi } from '../absensi.model';
import { AbsensiService } from '../service/absensi.service';

import { AbsensiFormService } from './absensi-form.service';
import { AbsensiUpdate } from './absensi-update';

describe('Absensi Management Update Component', () => {
  let comp: AbsensiUpdate;
  let fixture: ComponentFixture<AbsensiUpdate>;
  let activatedRoute: ActivatedRoute;
  let absensiFormService: AbsensiFormService;
  let absensiService: AbsensiService;
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

    fixture = TestBed.createComponent(AbsensiUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    absensiFormService = TestBed.inject(AbsensiFormService);
    absensiService = TestBed.inject(AbsensiService);
    pegawaiService = TestBed.inject(PegawaiService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Pegawai query and add missing value', () => {
      const absensi: IAbsensi = { id: 5760 };
      const pegawai: IPegawai = { id: 28349 };
      absensi.pegawai = pegawai;

      const pegawaiCollection: IPegawai[] = [{ id: 28349 }];
      vitest.spyOn(pegawaiService, 'query').mockReturnValue(of(new HttpResponse({ body: pegawaiCollection })));
      const additionalPegawais = [pegawai];
      const expectedCollection: IPegawai[] = [...additionalPegawais, ...pegawaiCollection];
      vitest.spyOn(pegawaiService, 'addPegawaiToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ absensi });
      comp.ngOnInit();

      expect(pegawaiService.query).toHaveBeenCalled();
      expect(pegawaiService.addPegawaiToCollectionIfMissing).toHaveBeenCalledWith(
        pegawaiCollection,
        ...additionalPegawais.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.pegawaisSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const absensi: IAbsensi = { id: 5760 };
      const pegawai: IPegawai = { id: 28349 };
      absensi.pegawai = pegawai;

      activatedRoute.data = of({ absensi });
      comp.ngOnInit();

      expect(comp.pegawaisSharedCollection()).toContainEqual(pegawai);
      expect(comp.absensi).toEqual(absensi);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAbsensi>();
      const absensi = { id: 18626 };
      vitest.spyOn(absensiFormService, 'getAbsensi').mockReturnValue(absensi);
      vitest.spyOn(absensiService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ absensi });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(absensi);
      saveSubject.complete();

      // THEN
      expect(absensiFormService.getAbsensi).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(absensiService.update).toHaveBeenCalledWith(expect.objectContaining(absensi));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAbsensi>();
      const absensi = { id: 18626 };
      vitest.spyOn(absensiFormService, 'getAbsensi').mockReturnValue({ id: null });
      vitest.spyOn(absensiService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ absensi: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(absensi);
      saveSubject.complete();

      // THEN
      expect(absensiFormService.getAbsensi).toHaveBeenCalled();
      expect(absensiService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IAbsensi>();
      const absensi = { id: 18626 };
      vitest.spyOn(absensiService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ absensi });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(absensiService.update).toHaveBeenCalled();
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
