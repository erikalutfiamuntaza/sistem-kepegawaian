import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IAbsensi, NewAbsensi } from '../absensi.model';

export type PartialUpdateAbsensi = Partial<IAbsensi> & Pick<IAbsensi, 'id'>;

type RestOf<T extends IAbsensi | NewAbsensi> = Omit<T, 'tanggal' | 'jamMasuk' | 'jamKeluar'> & {
  tanggal?: string | null;
  jamMasuk?: string | null;
  jamKeluar?: string | null;
};

export type RestAbsensi = RestOf<IAbsensi>;

export type NewRestAbsensi = RestOf<NewAbsensi>;

export type PartialUpdateRestAbsensi = RestOf<PartialUpdateAbsensi>;

@Injectable()
export class AbsensisService {
  readonly absensisParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly absensisResource = httpResource<RestAbsensi[]>(() => {
    const params = this.absensisParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of absensi that have been fetched. It is updated when the absensisResource emits a new value.
   * In case of error while fetching the absensis, the signal is set to an empty array.
   */
  readonly absensis = computed(() =>
    (this.absensisResource.hasValue() ? this.absensisResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/absensis');

  protected convertValueFromServer(restAbsensi: RestAbsensi): IAbsensi {
    return {
      ...restAbsensi,
      tanggal: restAbsensi.tanggal ? dayjs(restAbsensi.tanggal) : undefined,
      jamMasuk: restAbsensi.jamMasuk ? dayjs(restAbsensi.jamMasuk) : undefined,
      jamKeluar: restAbsensi.jamKeluar ? dayjs(restAbsensi.jamKeluar) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class AbsensiService extends AbsensisService {
  protected readonly http = inject(HttpClient);

  create(absensi: NewAbsensi): Observable<IAbsensi> {
    const copy = this.convertValueFromClient(absensi);
    return this.http.post<RestAbsensi>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(absensi: IAbsensi): Observable<IAbsensi> {
    const copy = this.convertValueFromClient(absensi);
    return this.http
      .put<RestAbsensi>(`${this.resourceUrl}/${encodeURIComponent(this.getAbsensiIdentifier(absensi))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(absensi: PartialUpdateAbsensi): Observable<IAbsensi> {
    const copy = this.convertValueFromClient(absensi);
    return this.http
      .patch<RestAbsensi>(`${this.resourceUrl}/${encodeURIComponent(this.getAbsensiIdentifier(absensi))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IAbsensi> {
    return this.http
      .get<RestAbsensi>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IAbsensi[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAbsensi[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getAbsensiIdentifier(absensi: Pick<IAbsensi, 'id'>): number {
    return absensi.id;
  }

  compareAbsensi(o1: Pick<IAbsensi, 'id'> | null, o2: Pick<IAbsensi, 'id'> | null): boolean {
    return o1 && o2 ? this.getAbsensiIdentifier(o1) === this.getAbsensiIdentifier(o2) : o1 === o2;
  }

  addAbsensiToCollectionIfMissing<Type extends Pick<IAbsensi, 'id'>>(
    absensiCollection: Type[],
    ...absensisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const absensis: Type[] = absensisToCheck.filter(isPresent);
    if (absensis.length > 0) {
      const absensiCollectionIdentifiers = absensiCollection.map(absensiItem => this.getAbsensiIdentifier(absensiItem));
      const absensisToAdd = absensis.filter(absensiItem => {
        const absensiIdentifier = this.getAbsensiIdentifier(absensiItem);
        if (absensiCollectionIdentifiers.includes(absensiIdentifier)) {
          return false;
        }
        absensiCollectionIdentifiers.push(absensiIdentifier);
        return true;
      });
      return [...absensisToAdd, ...absensiCollection];
    }
    return absensiCollection;
  }

  protected convertValueFromClient<T extends IAbsensi | NewAbsensi | PartialUpdateAbsensi>(absensi: T): RestOf<T> {
    return {
      ...absensi,
      tanggal: absensi.tanggal?.format(DATE_FORMAT) ?? null,
      jamMasuk: absensi.jamMasuk?.toJSON() ?? null,
      jamKeluar: absensi.jamKeluar?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestAbsensi): IAbsensi {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestAbsensi[]): IAbsensi[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
