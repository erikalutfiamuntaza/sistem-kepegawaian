import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../pegawai.test-samples';

import { PegawaiFormService } from './pegawai-form.service';

describe('Pegawai Form Service', () => {
  let service: PegawaiFormService;

  beforeEach(() => {
    service = TestBed.inject(PegawaiFormService);
  });

  describe('Service methods', () => {
    describe('createPegawaiFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPegawaiFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nama: expect.any(Object),
            jabatan: expect.any(Object),
            departemen: expect.any(Object),
            gaji: expect.any(Object),
          }),
        );
      });

      it('passing IPegawai should create a new form with FormGroup', () => {
        const formGroup = service.createPegawaiFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nama: expect.any(Object),
            jabatan: expect.any(Object),
            departemen: expect.any(Object),
            gaji: expect.any(Object),
          }),
        );
      });
    });

    describe('getPegawai', () => {
      it('should return NewPegawai for default Pegawai initial value', () => {
        const formGroup = service.createPegawaiFormGroup(sampleWithNewData);

        const pegawai = service.getPegawai(formGroup);

        expect(pegawai).toMatchObject(sampleWithNewData);
      });

      it('should return NewPegawai for empty Pegawai initial value', () => {
        const formGroup = service.createPegawaiFormGroup();

        const pegawai = service.getPegawai(formGroup);

        expect(pegawai).toMatchObject({});
      });

      it('should return IPegawai', () => {
        const formGroup = service.createPegawaiFormGroup(sampleWithRequiredData);

        const pegawai = service.getPegawai(formGroup);

        expect(pegawai).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPegawai should not enable id FormControl', () => {
        const formGroup = service.createPegawaiFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPegawai should disable id FormControl', () => {
        const formGroup = service.createPegawaiFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
