import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IPenggajian, NewPenggajian } from '../penggajian.model';

export type PartialUpdatePenggajian = Partial<IPenggajian> & Pick<IPenggajian, 'id'>;

type RestOf<T extends IPenggajian | NewPenggajian> = Omit<T, 'bulan'> & {
  bulan?: string | null;
};

export type RestPenggajian = RestOf<IPenggajian>;

export type NewRestPenggajian = RestOf<NewPenggajian>;

export type PartialUpdateRestPenggajian = RestOf<PartialUpdatePenggajian>;

@Injectable()
export class PenggajiansService {
  readonly penggajiansParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly penggajiansResource = httpResource<RestPenggajian[]>(() => {
    const params = this.penggajiansParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of penggajian that have been fetched. It is updated when the penggajiansResource emits a new value.
   * In case of error while fetching the penggajians, the signal is set to an empty array.
   */
  readonly penggajians = computed(() =>
    (this.penggajiansResource.hasValue() ? this.penggajiansResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/penggajians');

  protected convertValueFromServer(restPenggajian: RestPenggajian): IPenggajian {
    return {
      ...restPenggajian,
      bulan: restPenggajian.bulan ? dayjs(restPenggajian.bulan) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class PenggajianService extends PenggajiansService {
  protected readonly http = inject(HttpClient);

  create(penggajian: NewPenggajian): Observable<IPenggajian> {
    const copy = this.convertValueFromClient(penggajian);
    return this.http.post<RestPenggajian>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(penggajian: IPenggajian): Observable<IPenggajian> {
    const copy = this.convertValueFromClient(penggajian);
    return this.http
      .put<RestPenggajian>(`${this.resourceUrl}/${encodeURIComponent(this.getPenggajianIdentifier(penggajian))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(penggajian: PartialUpdatePenggajian): Observable<IPenggajian> {
    const copy = this.convertValueFromClient(penggajian);
    return this.http
      .patch<RestPenggajian>(`${this.resourceUrl}/${encodeURIComponent(this.getPenggajianIdentifier(penggajian))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPenggajian> {
    return this.http
      .get<RestPenggajian>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPenggajian[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPenggajian[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPenggajianIdentifier(penggajian: Pick<IPenggajian, 'id'>): number {
    return penggajian.id;
  }

  comparePenggajian(o1: Pick<IPenggajian, 'id'> | null, o2: Pick<IPenggajian, 'id'> | null): boolean {
    return o1 && o2 ? this.getPenggajianIdentifier(o1) === this.getPenggajianIdentifier(o2) : o1 === o2;
  }

  addPenggajianToCollectionIfMissing<Type extends Pick<IPenggajian, 'id'>>(
    penggajianCollection: Type[],
    ...penggajiansToCheck: (Type | null | undefined)[]
  ): Type[] {
    const penggajians: Type[] = penggajiansToCheck.filter(isPresent);
    if (penggajians.length > 0) {
      const penggajianCollectionIdentifiers = penggajianCollection.map(penggajianItem => this.getPenggajianIdentifier(penggajianItem));
      const penggajiansToAdd = penggajians.filter(penggajianItem => {
        const penggajianIdentifier = this.getPenggajianIdentifier(penggajianItem);
        if (penggajianCollectionIdentifiers.includes(penggajianIdentifier)) {
          return false;
        }
        penggajianCollectionIdentifiers.push(penggajianIdentifier);
        return true;
      });
      return [...penggajiansToAdd, ...penggajianCollection];
    }
    return penggajianCollection;
  }

  protected convertValueFromClient<T extends IPenggajian | NewPenggajian | PartialUpdatePenggajian>(penggajian: T): RestOf<T> {
    return {
      ...penggajian,
      bulan: penggajian.bulan?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPenggajian): IPenggajian {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPenggajian[]): IPenggajian[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
