import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'sistemKepegawaianApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'user-management',
    data: { pageTitle: 'userManagement.home.title' },
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'pegawai',
    data: { pageTitle: 'sistemKepegawaianApp.pegawai.home.title' },
    loadChildren: () => import('./pegawai/pegawai.routes'),
  },
  {
    path: 'cuti',
    data: { pageTitle: 'sistemKepegawaianApp.cuti.home.title' },
    loadChildren: () => import('./cuti/cuti.routes'),
  },
  {
    path: 'penggajian',
    data: { pageTitle: 'sistemKepegawaianApp.penggajian.home.title' },
    loadChildren: () => import('./penggajian/penggajian.routes'),
  },
  {
    path: 'absensi',
    data: { pageTitle: 'sistemKepegawaianApp.absensi.home.title' },
    loadChildren: () => import('./absensi/absensi.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
