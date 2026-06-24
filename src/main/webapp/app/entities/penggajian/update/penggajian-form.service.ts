import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPenggajian, NewPenggajian } from '../penggajian.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPenggajian for edit and NewPenggajianFormGroupInput for create.
 */
type PenggajianFormGroupInput = IPenggajian | PartialWithRequiredKeyOf<NewPenggajian>;

type PenggajianFormDefaults = Pick<NewPenggajian, 'id'>;

type PenggajianFormGroupContent = {
  id: FormControl<IPenggajian['id'] | NewPenggajian['id']>;
  bulan: FormControl<IPenggajian['bulan']>;
  gajiPokok: FormControl<IPenggajian['gajiPokok']>;
  bonus: FormControl<IPenggajian['bonus']>;
  potongan: FormControl<IPenggajian['potongan']>;
  totalGaji: FormControl<IPenggajian['totalGaji']>;
  pegawai: FormControl<IPenggajian['pegawai']>;
};

export type PenggajianFormGroup = FormGroup<PenggajianFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PenggajianFormService {
  createPenggajianFormGroup(penggajian?: PenggajianFormGroupInput): PenggajianFormGroup {
    const penggajianRawValue = {
      ...this.getFormDefaults(),
      ...(penggajian ?? { id: null }),
    };
    return new FormGroup<PenggajianFormGroupContent>({
      id: new FormControl(
        { value: penggajianRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      bulan: new FormControl(penggajianRawValue.bulan, {
        validators: [Validators.required],
      }),
      gajiPokok: new FormControl(penggajianRawValue.gajiPokok, {
        validators: [Validators.required],
      }),
      bonus: new FormControl(penggajianRawValue.bonus, {
        validators: [Validators.required],
      }),
      potongan: new FormControl(penggajianRawValue.potongan, {
        validators: [Validators.required],
      }),
      totalGaji: new FormControl(penggajianRawValue.totalGaji, {
        validators: [Validators.required],
      }),
      pegawai: new FormControl(penggajianRawValue.pegawai, {
        validators: [Validators.required],
      }),
    });
  }

  getPenggajian(form: PenggajianFormGroup): IPenggajian | NewPenggajian {
    return form.getRawValue();
  }

  resetForm(form: PenggajianFormGroup, penggajian: PenggajianFormGroupInput): void {
    const penggajianRawValue = { ...this.getFormDefaults(), ...penggajian };
    form.reset({
      ...penggajianRawValue,
      id: { value: penggajianRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PenggajianFormDefaults {
    return {
      id: null,
    };
  }
}
