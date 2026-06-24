import { IPegawai, NewPegawai } from './pegawai.model';

export const sampleWithRequiredData: IPegawai = {
  id: 6785,
  nama: 'silt per',
  jabatan: 'mob',
  departemen: 'questioningly',
  gaji: 9356.26,
};

export const sampleWithPartialData: IPegawai = {
  id: 18192,
  nama: 'fooey',
  jabatan: 'gah',
  departemen: 'incomparable',
  gaji: 20343.93,
};

export const sampleWithFullData: IPegawai = {
  id: 16725,
  nama: 'stool easily',
  jabatan: 'dissemble',
  departemen: 'until openly now',
  gaji: 22758,
};

export const sampleWithNewData: NewPegawai = {
  nama: 'blah grimy spice',
  jabatan: 'gigantic corporation',
  departemen: 'whoa furthermore until',
  gaji: 19632.34,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
