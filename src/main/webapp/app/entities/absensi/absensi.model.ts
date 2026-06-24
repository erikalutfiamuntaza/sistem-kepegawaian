import dayjs from 'dayjs/esm';

import { IPegawai } from 'app/entities/pegawai/pegawai.model';

export interface IAbsensi {
  id: number;
  tanggal?: dayjs.Dayjs | null;
  jamMasuk?: dayjs.Dayjs | null;
  jamKeluar?: dayjs.Dayjs | null;
  status?: string | null;
  pegawai?: Pick<IPegawai, 'id' | 'nama'> | null;
}

export type NewAbsensi = Omit<IAbsensi, 'id'> & { id: null };
