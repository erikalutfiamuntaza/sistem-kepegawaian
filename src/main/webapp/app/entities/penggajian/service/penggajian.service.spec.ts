import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPenggajian } from '../penggajian.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../penggajian.test-samples';

import { PenggajianService, RestPenggajian } from './penggajian.service';

const requireRestSample: RestPenggajian = {
  ...sampleWithRequiredData,
  bulan: sampleWithRequiredData.bulan?.format(DATE_FORMAT),
};

describe('Penggajian Service', () => {
  let service: PenggajianService;
  let httpMock: HttpTestingController;
  let expectedResult: IPenggajian | IPenggajian[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PenggajianService);
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

    it('should create a Penggajian', () => {
      const penggajian = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(penggajian).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Penggajian', () => {
      const penggajian = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(penggajian).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Penggajian', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Penggajian', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Penggajian', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests.length).toBe(1);
    });

    describe('addPenggajianToCollectionIfMissing', () => {
      it('should add a Penggajian to an empty array', () => {
        const penggajian: IPenggajian = sampleWithRequiredData;
        expectedResult = service.addPenggajianToCollectionIfMissing([], penggajian);
        expect(expectedResult).toEqual([penggajian]);
      });

      it('should not add a Penggajian to an array that contains it', () => {
        const penggajian: IPenggajian = sampleWithRequiredData;
        const penggajianCollection: IPenggajian[] = [
          {
            ...penggajian,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPenggajianToCollectionIfMissing(penggajianCollection, penggajian);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Penggajian to an array that doesn't contain it", () => {
        const penggajian: IPenggajian = sampleWithRequiredData;
        const penggajianCollection: IPenggajian[] = [sampleWithPartialData];
        expectedResult = service.addPenggajianToCollectionIfMissing(penggajianCollection, penggajian);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(penggajian);
      });

      it('should add only unique Penggajian to an array', () => {
        const penggajianArray: IPenggajian[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const penggajianCollection: IPenggajian[] = [sampleWithRequiredData];
        expectedResult = service.addPenggajianToCollectionIfMissing(penggajianCollection, ...penggajianArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const penggajian: IPenggajian = sampleWithRequiredData;
        const penggajian2: IPenggajian = sampleWithPartialData;
        expectedResult = service.addPenggajianToCollectionIfMissing([], penggajian, penggajian2);
        expect(expectedResult).toEqual([penggajian, penggajian2]);
      });

      it('should accept null and undefined values', () => {
        const penggajian: IPenggajian = sampleWithRequiredData;
        expectedResult = service.addPenggajianToCollectionIfMissing([], null, penggajian, undefined);
        expect(expectedResult).toEqual([penggajian]);
      });

      it('should return initial array if no Penggajian is added', () => {
        const penggajianCollection: IPenggajian[] = [sampleWithRequiredData];
        expectedResult = service.addPenggajianToCollectionIfMissing(penggajianCollection, undefined, null);
        expect(expectedResult).toEqual(penggajianCollection);
      });
    });

    describe('comparePenggajian', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePenggajian(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20594 };
        const entity2 = null;

        const compareResult1 = service.comparePenggajian(entity1, entity2);
        const compareResult2 = service.comparePenggajian(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20594 };
        const entity2 = { id: 16458 };

        const compareResult1 = service.comparePenggajian(entity1, entity2);
        const compareResult2 = service.comparePenggajian(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 20594 };
        const entity2 = { id: 20594 };

        const compareResult1 = service.comparePenggajian(entity1, entity2);
        const compareResult2 = service.comparePenggajian(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
