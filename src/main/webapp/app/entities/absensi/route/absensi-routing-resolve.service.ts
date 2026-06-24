import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IAbsensi } from '../absensi.model';
import { AbsensiService } from '../service/absensi.service';

const absensiResolve = (route: ActivatedRouteSnapshot): Observable<null | IAbsensi> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(AbsensiService);
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

export default absensiResolve;
