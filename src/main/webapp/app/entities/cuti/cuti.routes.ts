import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import CutiResolve from './route/cuti-routing-resolve.service';

const cutiRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cuti').then(m => m.Cuti),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cuti-detail').then(m => m.CutiDetail),
    resolve: {
      cuti: CutiResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cuti-update').then(m => m.CutiUpdate),
    resolve: {
      cuti: CutiResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cuti-update').then(m => m.CutiUpdate),
    resolve: {
      cuti: CutiResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cutiRoute;
