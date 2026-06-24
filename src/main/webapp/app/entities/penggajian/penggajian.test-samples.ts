import dayjs from 'dayjs/esm';

import { IPenggajian, NewPenggajian } from './penggajian.model';

export const sampleWithRequiredData: IPenggajian = {
  id: 31394,
  bulan: dayjs('2026-06-23'),
  gajiPokok: 26449.3,
  bonus: 13811.49,
  potongan: 8915.94,
  totalGaji: 2831.49,
};

export const sampleWithPartialData: IPenggajian = {
  id: 2556,
  bulan: dayjs('2026-06-23'),
  gajiPokok: 13959.7,
  bonus: 10393.21,
  potongan: 15743.6,
  totalGaji: 19397.69,
};

export const sampleWithFullData: IPenggajian = {
  id: 28750,
  bulan: dayjs('2026-06-24'),
  gajiPokok: 565.39,
  bonus: 23534.45,
  potongan: 19058.99,
  totalGaji: 30164.03,
};

export const sampleWithNewData: NewPenggajian = {
  bulan: dayjs('2026-06-24'),
  gajiPokok: 14956.65,
  bonus: 25439.73,
  potongan: 28029.25,
  totalGaji: 27190.57,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
