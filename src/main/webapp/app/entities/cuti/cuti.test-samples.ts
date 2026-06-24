import dayjs from 'dayjs/esm';

import { ICuti, NewCuti } from './cuti.model';

export const sampleWithRequiredData: ICuti = {
  id: 6073,
  tanggalMulai: dayjs('2026-06-24'),
  tanggalSelesai: dayjs('2026-06-23'),
  alasan: 'home cash',
  status: 'like since',
};

export const sampleWithPartialData: ICuti = {
  id: 14725,
  tanggalMulai: dayjs('2026-06-24'),
  tanggalSelesai: dayjs('2026-06-23'),
  alasan: 'sometimes boo',
  status: 'through determined',
};

export const sampleWithFullData: ICuti = {
  id: 6165,
  tanggalMulai: dayjs('2026-06-23'),
  tanggalSelesai: dayjs('2026-06-24'),
  alasan: 'into gadzooks whoever',
  status: 'dazzling whereas',
};

export const sampleWithNewData: NewCuti = {
  tanggalMulai: dayjs('2026-06-23'),
  tanggalSelesai: dayjs('2026-06-24'),
  alasan: 'mortally nor typewriter',
  status: 'preheat',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
