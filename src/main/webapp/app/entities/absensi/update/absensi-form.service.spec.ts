import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../absensi.test-samples';

import { AbsensiFormService } from './absensi-form.service';

describe('Absensi Form Service', () => {
  let service: AbsensiFormService;

  beforeEach(() => {
    service = TestBed.inject(AbsensiFormService);
  });

  describe('Service methods', () => {
    describe('createAbsensiFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAbsensiFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tanggal: expect.any(Object),
            jamMasuk: expect.any(Object),
            jamKeluar: expect.any(Object),
            status: expect.any(Object),
            pegawai: expect.any(Object),
          }),
        );
      });

      it('passing IAbsensi should create a new form with FormGroup', () => {
        const formGroup = service.createAbsensiFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tanggal: expect.any(Object),
            jamMasuk: expect.any(Object),
            jamKeluar: expect.any(Object),
            status: expect.any(Object),
            pegawai: expect.any(Object),
          }),
        );
      });
    });

    describe('getAbsensi', () => {
      it('should return NewAbsensi for default Absensi initial value', () => {
        const formGroup = service.createAbsensiFormGroup(sampleWithNewData);

        const absensi = service.getAbsensi(formGroup);

        expect(absensi).toMatchObject(sampleWithNewData);
      });

      it('should return NewAbsensi for empty Absensi initial value', () => {
        const formGroup = service.createAbsensiFormGroup();

        const absensi = service.getAbsensi(formGroup);

        expect(absensi).toMatchObject({});
      });

      it('should return IAbsensi', () => {
        const formGroup = service.createAbsensiFormGroup(sampleWithRequiredData);

        const absensi = service.getAbsensi(formGroup);

        expect(absensi).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAbsensi should not enable id FormControl', () => {
        const formGroup = service.createAbsensiFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAbsensi should disable id FormControl', () => {
        const formGroup = service.createAbsensiFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
