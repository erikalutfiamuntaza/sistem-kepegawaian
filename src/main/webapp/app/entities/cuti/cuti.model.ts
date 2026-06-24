import dayjs from 'dayjs/esm';

import { IPegawai } from 'app/entities/pegawai/pegawai.model';

export interface ICuti {
  id: number;
  tanggalMulai?: dayjs.Dayjs | null;
  tanggalSelesai?: dayjs.Dayjs | null;
  alasan?: string | null;
  status?: string | null;
  pegawai?: Pick<IPegawai, 'id' | 'nama'> | null;
}

export type NewCuti = Omit<ICuti, 'id'> & { id: null };
