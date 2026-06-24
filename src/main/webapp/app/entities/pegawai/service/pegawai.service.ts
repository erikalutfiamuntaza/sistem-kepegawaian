import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IPegawai, NewPegawai } from '../pegawai.model';

export type PartialUpdatePegawai = Partial<IPegawai> & Pick<IPegawai, 'id'>;

@Injectable()
export class PegawaisService {
  readonly pegawaisParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly pegawaisResource = httpResource<IPegawai[]>(() => {
    const params = this.pegawaisParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of pegawai that have been fetched. It is updated when the pegawaisResource emits a new value.
   * In case of error while fetching the pegawais, the signal is set to an empty array.
   */
  readonly pegawais = computed(() => (this.pegawaisResource.hasValue() ? this.pegawaisResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/pegawais');
}

@Injectable({ providedIn: 'root' })
export class PegawaiService extends PegawaisService {
  protected readonly http = inject(HttpClient);

  create(pegawai: NewPegawai): Observable<IPegawai> {
    return this.http.post<IPegawai>(this.resourceUrl, pegawai);
  }

  update(pegawai: IPegawai): Observable<IPegawai> {
    return this.http.put<IPegawai>(`${this.resourceUrl}/${encodeURIComponent(this.getPegawaiIdentifier(pegawai))}`, pegawai);
  }

  partialUpdate(pegawai: PartialUpdatePegawai): Observable<IPegawai> {
    return this.http.patch<IPegawai>(`${this.resourceUrl}/${encodeURIComponent(this.getPegawaiIdentifier(pegawai))}`, pegawai);
  }

  find(id: number): Observable<IPegawai> {
    return this.http.get<IPegawai>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPegawai[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPegawai[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPegawaiIdentifier(pegawai: Pick<IPegawai, 'id'>): number {
    return pegawai.id;
  }

  comparePegawai(o1: Pick<IPegawai, 'id'> | null, o2: Pick<IPegawai, 'id'> | null): boolean {
    return o1 && o2 ? this.getPegawaiIdentifier(o1) === this.getPegawaiIdentifier(o2) : o1 === o2;
  }

  addPegawaiToCollectionIfMissing<Type extends Pick<IPegawai, 'id'>>(
    pegawaiCollection: Type[],
    ...pegawaisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pegawais: Type[] = pegawaisToCheck.filter(isPresent);
    if (pegawais.length > 0) {
      const pegawaiCollectionIdentifiers = pegawaiCollection.map(pegawaiItem => this.getPegawaiIdentifier(pegawaiItem));
      const pegawaisToAdd = pegawais.filter(pegawaiItem => {
        const pegawaiIdentifier = this.getPegawaiIdentifier(pegawaiItem);
        if (pegawaiCollectionIdentifiers.includes(pegawaiIdentifier)) {
          return false;
        }
        pegawaiCollectionIdentifiers.push(pegawaiIdentifier);
        return true;
      });
      return [...pegawaisToAdd, ...pegawaiCollection];
    }
    return pegawaiCollection;
  }
}
