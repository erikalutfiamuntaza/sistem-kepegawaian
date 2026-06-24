import dayjs from 'dayjs/esm';

import { IAbsensi, NewAbsensi } from './absensi.model';

export const sampleWithRequiredData: IAbsensi = {
  id: 11212,
  tanggal: dayjs('2026-06-23'),
  jamMasuk: dayjs('2026-06-23T17:51'),
  jamKeluar: dayjs('2026-06-24T02:01'),
  status: 'enormously',
};

export const sampleWithPartialData: IAbsensi = {
  id: 9864,
  tanggal: dayjs('2026-06-23'),
  jamMasuk: dayjs('2026-06-23T17:50'),
  jamKeluar: dayjs('2026-06-23T14:36'),
  status: 'same jell by',
};

export const sampleWithFullData: IAbsensi = {
  id: 763,
  tanggal: dayjs('2026-06-23'),
  jamMasuk: dayjs('2026-06-23T14:26'),
  jamKeluar: dayjs('2026-06-24T07:01'),
  status: 'tattered',
};

export const sampleWithNewData: NewAbsensi = {
  tanggal: dayjs('2026-06-24'),
  jamMasuk: dayjs('2026-06-23T20:34'),
  jamKeluar: dayjs('2026-06-23T14:51'),
  status: 'regarding bitterly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
