import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAbsensi } from '../absensi.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../absensi.test-samples';

import { AbsensiService, RestAbsensi } from './absensi.service';

const requireRestSample: RestAbsensi = {
  ...sampleWithRequiredData,
  tanggal: sampleWithRequiredData.tanggal?.format(DATE_FORMAT),
  jamMasuk: sampleWithRequiredData.jamMasuk?.toJSON(),
  jamKeluar: sampleWithRequiredData.jamKeluar?.toJSON(),
};

describe('Absensi Service', () => {
  let service: AbsensiService;
  let httpMock: HttpTestingController;
  let expectedResult: IAbsensi | IAbsensi[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AbsensiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Absensi', () => {
      const absensi = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(absensi).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Absensi', () => {
      const absensi = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(absensi).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Absensi', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Absensi', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Absensi', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests.length).toBe(1);
    });

    describe('addAbsensiToCollectionIfMissing', () => {
      it('should add a Absensi to an empty array', () => {
        const absensi: IAbsensi = sampleWithRequiredData;
        expectedResult = service.addAbsensiToCollectionIfMissing([], absensi);
        expect(expectedResult).toEqual([absensi]);
      });

      it('should not add a Absensi to an array that contains it', () => {
        const absensi: IAbsensi = sampleWithRequiredData;
        const absensiCollection: IAbsensi[] = [
          {
            ...absensi,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAbsensiToCollectionIfMissing(absensiCollection, absensi);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Absensi to an array that doesn't contain it", () => {
        const absensi: IAbsensi = sampleWithRequiredData;
        const absensiCollection: IAbsensi[] = [sampleWithPartialData];
        expectedResult = service.addAbsensiToCollectionIfMissing(absensiCollection, absensi);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(absensi);
      });

      it('should add only unique Absensi to an array', () => {
        const absensiArray: IAbsensi[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const absensiCollection: IAbsensi[] = [sampleWithRequiredData];
        expectedResult = service.addAbsensiToCollectionIfMissing(absensiCollection, ...absensiArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const absensi: IAbsensi = sampleWithRequiredData;
        const absensi2: IAbsensi = sampleWithPartialData;
        expectedResult = service.addAbsensiToCollectionIfMissing([], absensi, absensi2);
        expect(expectedResult).toEqual([absensi, absensi2]);
      });

      it('should accept null and undefined values', () => {
        const absensi: IAbsensi = sampleWithRequiredData;
        expectedResult = service.addAbsensiToCollectionIfMissing([], null, absensi, undefined);
        expect(expectedResult).toEqual([absensi]);
      });

      it('should return initial array if no Absensi is added', () => {
        const absensiCollection: IAbsensi[] = [sampleWithRequiredData];
        expectedResult = service.addAbsensiToCollectionIfMissing(absensiCollection, undefined, null);
        expect(expectedResult).toEqual(absensiCollection);
      });
    });

    describe('compareAbsensi', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAbsensi(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18626 };
        const entity2 = null;

        const compareResult1 = service.compareAbsensi(entity1, entity2);
        const compareResult2 = service.compareAbsensi(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18626 };
        const entity2 = { id: 5760 };

        const compareResult1 = service.compareAbsensi(entity1, entity2);
        const compareResult2 = service.compareAbsensi(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18626 };
        const entity2 = { id: 18626 };

        const compareResult1 = service.compareAbsensi(entity1, entity2);
        const compareResult2 = service.compareAbsensi(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
