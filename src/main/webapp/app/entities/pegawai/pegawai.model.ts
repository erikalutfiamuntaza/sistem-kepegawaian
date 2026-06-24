export interface IPegawai {
  id: number;
  nama?: string | null;
  jabatan?: string | null;
  departemen?: string | null;
  gaji?: number | null;
}

export type NewPegawai = Omit<IPegawai, 'id'> & { id: null };
