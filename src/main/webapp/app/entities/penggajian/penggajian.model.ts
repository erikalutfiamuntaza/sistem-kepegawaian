import dayjs from 'dayjs/esm';

import { IPegawai } from 'app/entities/pegawai/pegawai.model';

export interface IPenggajian {
  id: number;
  bulan?: dayjs.Dayjs | null;
  gajiPokok?: number | null;
  bonus?: number | null;
  potongan?: number | null;
  totalGaji?: number | null;
  pegawai?: Pick<IPegawai, 'id' | 'nama'> | null;
}

export type NewPenggajian = Omit<IPenggajian, 'id'> & { id: null };
