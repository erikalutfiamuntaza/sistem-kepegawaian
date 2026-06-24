import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICuti, NewCuti } from '../cuti.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICuti for edit and NewCutiFormGroupInput for create.
 */
type CutiFormGroupInput = ICuti | PartialWithRequiredKeyOf<NewCuti>;

type CutiFormDefaults = Pick<NewCuti, 'id'>;

type CutiFormGroupContent = {
  id: FormControl<ICuti['id'] | NewCuti['id']>;
  tanggalMulai: FormControl<ICuti['tanggalMulai']>;
  tanggalSelesai: FormControl<ICuti['tanggalSelesai']>;
  alasan: FormControl<ICuti['alasan']>;
  status: FormControl<ICuti['status']>;
  pegawai: FormControl<ICuti['pegawai']>;
};

export type CutiFormGroup = FormGroup<CutiFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CutiFormService {
  createCutiFormGroup(cuti?: CutiFormGroupInput): CutiFormGroup {
    const cutiRawValue = {
      ...this.getFormDefaults(),
      ...(cuti ?? { id: null }),
    };
    return new FormGroup<CutiFormGroupContent>({
      id: new FormControl(
        { value: cutiRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tanggalMulai: new FormControl(cutiRawValue.tanggalMulai, {
        validators: [Validators.required],
      }),
      tanggalSelesai: new FormControl(cutiRawValue.tanggalSelesai, {
        validators: [Validators.required],
      }),
      alasan: new FormControl(cutiRawValue.alasan, {
        validators: [Validators.required],
      }),
      status: new FormControl(cutiRawValue.status, {
        validators: [Validators.required],
      }),
      pegawai: new FormControl(cutiRawValue.pegawai, {
        validators: [Validators.required],
      }),
    });
  }

  getCuti(form: CutiFormGroup): ICuti | NewCuti {
    return form.getRawValue();
  }

  resetForm(form: CutiFormGroup, cuti: CutiFormGroupInput): void {
    const cutiRawValue = { ...this.getFormDefaults(), ...cuti };
    form.reset({
      ...cutiRawValue,
      id: { value: cutiRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CutiFormDefaults {
    return {
      id: null,
    };
  }
}
