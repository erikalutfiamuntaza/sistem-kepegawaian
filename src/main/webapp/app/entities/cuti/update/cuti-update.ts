import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslateModule } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IPegawai } from 'app/entities/pegawai/pegawai.model';
import { PegawaiService } from 'app/entities/pegawai/service/pegawai.service';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ICuti } from '../cuti.model';
import { CutiService } from '../service/cuti.service';

import { CutiFormGroup, CutiFormService } from './cuti-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-cuti-update',
  templateUrl: './cuti-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class CutiUpdate implements OnInit {
  readonly isSaving = signal(false);
  cuti: ICuti | null = null;

  pegawaisSharedCollection = signal<IPegawai[]>([]);

  protected cutiService = inject(CutiService);
  protected cutiFormService = inject(CutiFormService);
  protected pegawaiService = inject(PegawaiService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CutiFormGroup = this.cutiFormService.createCutiFormGroup();

  comparePegawai = (o1: IPegawai | null, o2: IPegawai | null): boolean => this.pegawaiService.comparePegawai(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cuti }) => {
      this.cuti = cuti;
      if (cuti) {
        this.updateForm(cuti);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const cuti = this.cutiFormService.getCuti(this.editForm);
    if (cuti.id === null) {
      this.subscribeToSaveResponse(this.cutiService.create(cuti));
    } else {
      this.subscribeToSaveResponse(this.cutiService.update(cuti));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICuti | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(cuti: ICuti): void {
    this.cuti = cuti;
    this.cutiFormService.resetForm(this.editForm, cuti);

    this.pegawaisSharedCollection.update(pegawais => this.pegawaiService.addPegawaiToCollectionIfMissing<IPegawai>(pegawais, cuti.pegawai));
  }

  protected loadRelationshipsOptions(): void {
    this.pegawaiService
      .query()
      .pipe(map((res: HttpResponse<IPegawai[]>) => res.body ?? []))
      .pipe(map((pegawais: IPegawai[]) => this.pegawaiService.addPegawaiToCollectionIfMissing<IPegawai>(pegawais, this.cuti?.pegawai)))
      .subscribe((pegawais: IPegawai[]) => this.pegawaisSharedCollection.set(pegawais));
  }
}
