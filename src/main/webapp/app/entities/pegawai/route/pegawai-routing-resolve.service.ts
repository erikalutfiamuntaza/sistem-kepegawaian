import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPegawai } from '../pegawai.model';
import { PegawaiService } from '../service/pegawai.service';

const pegawaiResolve = (route: ActivatedRouteSnapshot): Observable<null | IPegawai> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PegawaiService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default pegawaiResolve;
