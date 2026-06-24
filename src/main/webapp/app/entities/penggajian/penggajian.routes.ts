import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import PenggajianResolve from './route/penggajian-routing-resolve.service';

const penggajianRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/penggajian').then(m => m.Penggajian),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/penggajian-detail').then(m => m.PenggajianDetail),
    resolve: {
      penggajian: PenggajianResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/penggajian-update').then(m => m.PenggajianUpdate),
    resolve: {
      penggajian: PenggajianResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/penggajian-update').then(m => m.PenggajianUpdate),
    resolve: {
      penggajian: PenggajianResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default penggajianRoute;
