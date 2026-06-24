import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cuti.test-samples';

import { CutiFormService } from './cuti-form.service';

describe('Cuti Form Service', () => {
  let service: CutiFormService;

  beforeEach(() => {
    service = TestBed.inject(CutiFormService);
  });

  describe('Service methods', () => {
    describe('createCutiFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCutiFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tanggalMulai: expect.any(Object),
            tanggalSelesai: expect.any(Object),
            alasan: expect.any(Object),
            status: expect.any(Object),
            pegawai: expect.any(Object),
          }),
        );
      });

      it('passing ICuti should create a new form with FormGroup', () => {
        const formGroup = service.createCutiFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tanggalMulai: expect.any(Object),
            tanggalSelesai: expect.any(Object),
            alasan: expect.any(Object),
            status: expect.any(Object),
            pegawai: expect.any(Object),
          }),
        );
      });
    });

    describe('getCuti', () => {
      it('should return NewCuti for default Cuti initial value', () => {
        const formGroup = service.createCutiFormGroup(sampleWithNewData);

        const cuti = service.getCuti(formGroup);

        expect(cuti).toMatchObject(sampleWithNewData);
      });

      it('should return NewCuti for empty Cuti initial value', () => {
        const formGroup = service.createCutiFormGroup();

        const cuti = service.getCuti(formGroup);

        expect(cuti).toMatchObject({});
      });

      it('should return ICuti', () => {
        const formGroup = service.createCutiFormGroup(sampleWithRequiredData);

        const cuti = service.getCuti(formGroup);

        expect(cuti).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICuti should not enable id FormControl', () => {
        const formGroup = service.createCutiFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCuti should disable id FormControl', () => {
        const formGroup = service.createCutiFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
