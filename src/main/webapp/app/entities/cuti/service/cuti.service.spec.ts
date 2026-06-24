import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICuti } from '../cuti.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cuti.test-samples';

import { CutiService, RestCuti } from './cuti.service';

const requireRestSample: RestCuti = {
  ...sampleWithRequiredData,
  tanggalMulai: sampleWithRequiredData.tanggalMulai?.format(DATE_FORMAT),
  tanggalSelesai: sampleWithRequiredData.tanggalSelesai?.format(DATE_FORMAT),
};

describe('Cuti Service', () => {
  let service: CutiService;
  let httpMock: HttpTestingController;
  let expectedResult: ICuti | ICuti[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CutiService);
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

    it('should create a Cuti', () => {
      const cuti = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cuti).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cuti', () => {
      const cuti = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cuti).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cuti', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cuti', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cuti', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests.length).toBe(1);
    });

    describe('addCutiToCollectionIfMissing', () => {
      it('should add a Cuti to an empty array', () => {
        const cuti: ICuti = sampleWithRequiredData;
        expectedResult = service.addCutiToCollectionIfMissing([], cuti);
        expect(expectedResult).toEqual([cuti]);
      });

      it('should not add a Cuti to an array that contains it', () => {
        const cuti: ICuti = sampleWithRequiredData;
        const cutiCollection: ICuti[] = [
          {
            ...cuti,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCutiToCollectionIfMissing(cutiCollection, cuti);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cuti to an array that doesn't contain it", () => {
        const cuti: ICuti = sampleWithRequiredData;
        const cutiCollection: ICuti[] = [sampleWithPartialData];
        expectedResult = service.addCutiToCollectionIfMissing(cutiCollection, cuti);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cuti);
      });

      it('should add only unique Cuti to an array', () => {
        const cutiArray: ICuti[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cutiCollection: ICuti[] = [sampleWithRequiredData];
        expectedResult = service.addCutiToCollectionIfMissing(cutiCollection, ...cutiArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cuti: ICuti = sampleWithRequiredData;
        const cuti2: ICuti = sampleWithPartialData;
        expectedResult = service.addCutiToCollectionIfMissing([], cuti, cuti2);
        expect(expectedResult).toEqual([cuti, cuti2]);
      });

      it('should accept null and undefined values', () => {
        const cuti: ICuti = sampleWithRequiredData;
        expectedResult = service.addCutiToCollectionIfMissing([], null, cuti, undefined);
        expect(expectedResult).toEqual([cuti]);
      });

      it('should return initial array if no Cuti is added', () => {
        const cutiCollection: ICuti[] = [sampleWithRequiredData];
        expectedResult = service.addCutiToCollectionIfMissing(cutiCollection, undefined, null);
        expect(expectedResult).toEqual(cutiCollection);
      });
    });

    describe('compareCuti', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCuti(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2717 };
        const entity2 = null;

        const compareResult1 = service.compareCuti(entity1, entity2);
        const compareResult2 = service.compareCuti(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2717 };
        const entity2 = { id: 14574 };

        const compareResult1 = service.compareCuti(entity1, entity2);
        const compareResult2 = service.compareCuti(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2717 };
        const entity2 = { id: 2717 };

        const compareResult1 = service.compareCuti(entity1, entity2);
        const compareResult2 = service.compareCuti(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
