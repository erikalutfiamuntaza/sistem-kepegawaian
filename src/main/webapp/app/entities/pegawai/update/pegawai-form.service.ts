import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPegawai, NewPegawai } from '../pegawai.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPegawai for edit and NewPegawaiFormGroupInput for create.
 */
type PegawaiFormGroupInput = IPegawai | PartialWithRequiredKeyOf<NewPegawai>;

type PegawaiFormDefaults = Pick<NewPegawai, 'id'>;

type PegawaiFormGroupContent = {
  id: FormControl<IPegawai['id'] | NewPegawai['id']>;
  nama: FormControl<IPegawai['nama']>;
  jabatan: FormControl<IPegawai['jabatan']>;
  departemen: FormControl<IPegawai['departemen']>;
  gaji: FormControl<IPegawai['gaji']>;
};

export type PegawaiFormGroup = FormGroup<PegawaiFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PegawaiFormService {
  createPegawaiFormGroup(pegawai?: PegawaiFormGroupInput): PegawaiFormGroup {
    const pegawaiRawValue = {
      ...this.getFormDefaults(),
      ...(pegawai ?? { id: null }),
    };
    return new FormGroup<PegawaiFormGroupContent>({
      id: new FormControl(
        { value: pegawaiRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nama: new FormControl(pegawaiRawValue.nama, {
        validators: [Validators.required],
      }),
      jabatan: new FormControl(pegawaiRawValue.jabatan, {
        validators: [Validators.required],
      }),
      departemen: new FormControl(pegawaiRawValue.departemen, {
        validators: [Validators.required],
      }),
      gaji: new FormControl(pegawaiRawValue.gaji, {
        validators: [Validators.required],
      }),
    });
  }

  getPegawai(form: PegawaiFormGroup): IPegawai | NewPegawai {
    return form.getRawValue();
  }

  resetForm(form: PegawaiFormGroup, pegawai: PegawaiFormGroupInput): void {
    const pegawaiRawValue = { ...this.getFormDefaults(), ...pegawai };
    form.reset({
      ...pegawaiRawValue,
      id: { value: pegawaiRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PegawaiFormDefaults {
    return {
      id: null,
    };
  }
}
