import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ICuti, NewCuti } from '../cuti.model';

export type PartialUpdateCuti = Partial<ICuti> & Pick<ICuti, 'id'>;

type RestOf<T extends ICuti | NewCuti> = Omit<T, 'tanggalMulai' | 'tanggalSelesai'> & {
  tanggalMulai?: string | null;
  tanggalSelesai?: string | null;
};

export type RestCuti = RestOf<ICuti>;

export type NewRestCuti = RestOf<NewCuti>;

export type PartialUpdateRestCuti = RestOf<PartialUpdateCuti>;

@Injectable()
export class CutisService {
  readonly cutisParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly cutisResource = httpResource<RestCuti[]>(() => {
    const params = this.cutisParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of cuti that have been fetched. It is updated when the cutisResource emits a new value.
   * In case of error while fetching the cutis, the signal is set to an empty array.
   */
  readonly cutis = computed(() =>
    (this.cutisResource.hasValue() ? this.cutisResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/cutis');

  protected convertValueFromServer(restCuti: RestCuti): ICuti {
    return {
      ...restCuti,
      tanggalMulai: restCuti.tanggalMulai ? dayjs(restCuti.tanggalMulai) : undefined,
      tanggalSelesai: restCuti.tanggalSelesai ? dayjs(restCuti.tanggalSelesai) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class CutiService extends CutisService {
  protected readonly http = inject(HttpClient);

  create(cuti: NewCuti): Observable<ICuti> {
    const copy = this.convertValueFromClient(cuti);
    return this.http.post<RestCuti>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cuti: ICuti): Observable<ICuti> {
    const copy = this.convertValueFromClient(cuti);
    return this.http
      .put<RestCuti>(`${this.resourceUrl}/${encodeURIComponent(this.getCutiIdentifier(cuti))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cuti: PartialUpdateCuti): Observable<ICuti> {
    const copy = this.convertValueFromClient(cuti);
    return this.http
      .patch<RestCuti>(`${this.resourceUrl}/${encodeURIComponent(this.getCutiIdentifier(cuti))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICuti> {
    return this.http.get<RestCuti>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICuti[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCuti[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCutiIdentifier(cuti: Pick<ICuti, 'id'>): number {
    return cuti.id;
  }

  compareCuti(o1: Pick<ICuti, 'id'> | null, o2: Pick<ICuti, 'id'> | null): boolean {
    return o1 && o2 ? this.getCutiIdentifier(o1) === this.getCutiIdentifier(o2) : o1 === o2;
  }

  addCutiToCollectionIfMissing<Type extends Pick<ICuti, 'id'>>(
    cutiCollection: Type[],
    ...cutisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cutis: Type[] = cutisToCheck.filter(isPresent);
    if (cutis.length > 0) {
      const cutiCollectionIdentifiers = cutiCollection.map(cutiItem => this.getCutiIdentifier(cutiItem));
      const cutisToAdd = cutis.filter(cutiItem => {
        const cutiIdentifier = this.getCutiIdentifier(cutiItem);
        if (cutiCollectionIdentifiers.includes(cutiIdentifier)) {
          return false;
        }
        cutiCollectionIdentifiers.push(cutiIdentifier);
        return true;
      });
      return [...cutisToAdd, ...cutiCollection];
    }
    return cutiCollection;
  }

  protected convertValueFromClient<T extends ICuti | NewCuti | PartialUpdateCuti>(cuti: T): RestOf<T> {
    return {
      ...cuti,
      tanggalMulai: cuti.tanggalMulai?.format(DATE_FORMAT) ?? null,
      tanggalSelesai: cuti.tanggalSelesai?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCuti): ICuti {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCuti[]): ICuti[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
