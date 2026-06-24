import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPegawai } from '../pegawai.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../pegawai.test-samples';

import { PegawaiService } from './pegawai.service';

const requireRestSample: IPegawai = {
  ...sampleWithRequiredData,
};

describe('Pegawai Service', () => {
  let service: PegawaiService;
  let httpMock: HttpTestingController;
  let expectedResult: IPegawai | IPegawai[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PegawaiService);
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

    it('should create a Pegawai', () => {
      const pegawai = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pegawai).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pegawai', () => {
      const pegawai = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pegawai).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pegawai', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pegawai', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pegawai', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests.length).toBe(1);
    });

    describe('addPegawaiToCollectionIfMissing', () => {
      it('should add a Pegawai to an empty array', () => {
        const pegawai: IPegawai = sampleWithRequiredData;
        expectedResult = service.addPegawaiToCollectionIfMissing([], pegawai);
        expect(expectedResult).toEqual([pegawai]);
      });

      it('should not add a Pegawai to an array that contains it', () => {
        const pegawai: IPegawai = sampleWithRequiredData;
        const pegawaiCollection: IPegawai[] = [
          {
            ...pegawai,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPegawaiToCollectionIfMissing(pegawaiCollection, pegawai);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pegawai to an array that doesn't contain it", () => {
        const pegawai: IPegawai = sampleWithRequiredData;
        const pegawaiCollection: IPegawai[] = [sampleWithPartialData];
        expectedResult = service.addPegawaiToCollectionIfMissing(pegawaiCollection, pegawai);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pegawai);
      });

      it('should add only unique Pegawai to an array', () => {
        const pegawaiArray: IPegawai[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pegawaiCollection: IPegawai[] = [sampleWithRequiredData];
        expectedResult = service.addPegawaiToCollectionIfMissing(pegawaiCollection, ...pegawaiArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pegawai: IPegawai = sampleWithRequiredData;
        const pegawai2: IPegawai = sampleWithPartialData;
        expectedResult = service.addPegawaiToCollectionIfMissing([], pegawai, pegawai2);
        expect(expectedResult).toEqual([pegawai, pegawai2]);
      });

      it('should accept null and undefined values', () => {
        const pegawai: IPegawai = sampleWithRequiredData;
        expectedResult = service.addPegawaiToCollectionIfMissing([], null, pegawai, undefined);
        expect(expectedResult).toEqual([pegawai]);
      });

      it('should return initial array if no Pegawai is added', () => {
        const pegawaiCollection: IPegawai[] = [sampleWithRequiredData];
        expectedResult = service.addPegawaiToCollectionIfMissing(pegawaiCollection, undefined, null);
        expect(expectedResult).toEqual(pegawaiCollection);
      });
    });

    describe('comparePegawai', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePegawai(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28349 };
        const entity2 = null;

        const compareResult1 = service.comparePegawai(entity1, entity2);
        const compareResult2 = service.comparePegawai(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28349 };
        const entity2 = { id: 15805 };

        const compareResult1 = service.comparePegawai(entity1, entity2);
        const compareResult2 = service.comparePegawai(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 28349 };
        const entity2 = { id: 28349 };

        const compareResult1 = service.comparePegawai(entity1, entity2);
        const compareResult2 = service.comparePegawai(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
