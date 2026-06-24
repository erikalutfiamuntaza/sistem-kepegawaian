import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../penggajian.test-samples';

import { PenggajianFormService } from './penggajian-form.service';

describe('Penggajian Form Service', () => {
  let service: PenggajianFormService;

  beforeEach(() => {
    service = TestBed.inject(PenggajianFormService);
  });

  describe('Service methods', () => {
    describe('createPenggajianFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPenggajianFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bulan: expect.any(Object),
            gajiPokok: expect.any(Object),
            bonus: expect.any(Object),
            potongan: expect.any(Object),
            totalGaji: expect.any(Object),
            pegawai: expect.any(Object),
          }),
        );
      });

      it('passing IPenggajian should create a new form with FormGroup', () => {
        const formGroup = service.createPenggajianFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bulan: expect.any(Object),
            gajiPokok: expect.any(Object),
            bonus: expect.any(Object),
            potongan: expect.any(Object),
            totalGaji: expect.any(Object),
            pegawai: expect.any(Object),
          }),
        );
      });
    });

    describe('getPenggajian', () => {
      it('should return NewPenggajian for default Penggajian initial value', () => {
        const formGroup = service.createPenggajianFormGroup(sampleWithNewData);

        const penggajian = service.getPenggajian(formGroup);

        expect(penggajian).toMatchObject(sampleWithNewData);
      });

      it('should return NewPenggajian for empty Penggajian initial value', () => {
        const formGroup = service.createPenggajianFormGroup();

        const penggajian = service.getPenggajian(formGroup);

        expect(penggajian).toMatchObject({});
      });

      it('should return IPenggajian', () => {
        const formGroup = service.createPenggajianFormGroup(sampleWithRequiredData);

        const penggajian = service.getPenggajian(formGroup);

        expect(penggajian).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPenggajian should not enable id FormControl', () => {
        const formGroup = service.createPenggajianFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPenggajian should disable id FormControl', () => {
        const formGroup = service.createPenggajianFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
