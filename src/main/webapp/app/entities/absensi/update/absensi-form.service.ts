import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAbsensi, NewAbsensi } from '../absensi.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAbsensi for edit and NewAbsensiFormGroupInput for create.
 */
type AbsensiFormGroupInput = IAbsensi | PartialWithRequiredKeyOf<NewAbsensi>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAbsensi | NewAbsensi> = Omit<T, 'jamMasuk' | 'jamKeluar'> & {
  jamMasuk?: string | null;
  jamKeluar?: string | null;
};

type AbsensiFormRawValue = FormValueOf<IAbsensi>;

type NewAbsensiFormRawValue = FormValueOf<NewAbsensi>;

type AbsensiFormDefaults = Pick<NewAbsensi, 'id' | 'jamMasuk' | 'jamKeluar'>;

type AbsensiFormGroupContent = {
  id: FormControl<AbsensiFormRawValue['id'] | NewAbsensi['id']>;
  tanggal: FormControl<AbsensiFormRawValue['tanggal']>;
  jamMasuk: FormControl<AbsensiFormRawValue['jamMasuk']>;
  jamKeluar: FormControl<AbsensiFormRawValue['jamKeluar']>;
  status: FormControl<AbsensiFormRawValue['status']>;
  pegawai: FormControl<AbsensiFormRawValue['pegawai']>;
};

export type AbsensiFormGroup = FormGroup<AbsensiFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AbsensiFormService {
  createAbsensiFormGroup(absensi?: AbsensiFormGroupInput): AbsensiFormGroup {
    const absensiRawValue = this.convertAbsensiToAbsensiRawValue({
      ...this.getFormDefaults(),
      ...(absensi ?? { id: null }),
    });
    return new FormGroup<AbsensiFormGroupContent>({
      id: new FormControl(
        { value: absensiRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tanggal: new FormControl(absensiRawValue.tanggal, {
        validators: [Validators.required],
      }),
      jamMasuk: new FormControl(absensiRawValue.jamMasuk, {
        validators: [Validators.required],
      }),
      jamKeluar: new FormControl(absensiRawValue.jamKeluar, {
        validators: [Validators.required],
      }),
      status: new FormControl(absensiRawValue.status, {
        validators: [Validators.required],
      }),
      pegawai: new FormControl(absensiRawValue.pegawai, {
        validators: [Validators.required],
      }),
    });
  }

  getAbsensi(form: AbsensiFormGroup): IAbsensi | NewAbsensi {
    return this.convertAbsensiRawValueToAbsensi(form.getRawValue());
  }

  resetForm(form: AbsensiFormGroup, absensi: AbsensiFormGroupInput): void {
    const absensiRawValue = this.convertAbsensiToAbsensiRawValue({ ...this.getFormDefaults(), ...absensi });
    form.reset({
      ...absensiRawValue,
      id: { value: absensiRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): AbsensiFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      jamMasuk: currentTime,
      jamKeluar: currentTime,
    };
  }

  private convertAbsensiRawValueToAbsensi(rawAbsensi: AbsensiFormRawValue | NewAbsensiFormRawValue): IAbsensi | NewAbsensi {
    return {
      ...rawAbsensi,
      jamMasuk: dayjs(rawAbsensi.jamMasuk, DATE_TIME_FORMAT),
      jamKeluar: dayjs(rawAbsensi.jamKeluar, DATE_TIME_FORMAT),
    };
  }

  private convertAbsensiToAbsensiRawValue(
    absensi: IAbsensi | (Partial<NewAbsensi> & AbsensiFormDefaults),
  ): AbsensiFormRawValue | PartialWithRequiredKeyOf<NewAbsensiFormRawValue> {
    return {
      ...absensi,
      jamMasuk: absensi.jamMasuk ? absensi.jamMasuk.format(DATE_TIME_FORMAT) : undefined,
      jamKeluar: absensi.jamKeluar ? absensi.jamKeluar.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
