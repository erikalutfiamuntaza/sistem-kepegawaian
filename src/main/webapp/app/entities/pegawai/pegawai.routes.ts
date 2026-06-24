import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import PegawaiResolve from './route/pegawai-routing-resolve.service';

const pegawaiRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pegawai').then(m => m.Pegawai),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pegawai-detail').then(m => m.PegawaiDetail),
    resolve: {
      pegawai: PegawaiResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pegawai-update').then(m => m.PegawaiUpdate),
    resolve: {
      pegawai: PegawaiResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pegawai-update').then(m => m.PegawaiUpdate),
    resolve: {
      pegawai: PegawaiResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pegawaiRoute;
