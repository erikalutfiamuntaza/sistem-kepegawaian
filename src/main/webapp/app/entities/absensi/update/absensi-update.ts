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
import { IAbsensi } from '../absensi.model';
import { AbsensiService } from '../service/absensi.service';

import { AbsensiFormGroup, AbsensiFormService } from './absensi-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-absensi-update',
  templateUrl: './absensi-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class AbsensiUpdate implements OnInit {
  readonly isSaving = signal(false);
  absensi: IAbsensi | null = null;

  pegawaisSharedCollection = signal<IPegawai[]>([]);

  protected absensiService = inject(AbsensiService);
  protected absensiFormService = inject(AbsensiFormService);
  protected pegawaiService = inject(PegawaiService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AbsensiFormGroup = this.absensiFormService.createAbsensiFormGroup();

  comparePegawai = (o1: IPegawai | null, o2: IPegawai | null): boolean => this.pegawaiService.comparePegawai(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ absensi }) => {
      this.absensi = absensi;
      if (absensi) {
        this.updateForm(absensi);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const absensi = this.absensiFormService.getAbsensi(this.editForm);
    if (absensi.id === null) {
      this.subscribeToSaveResponse(this.absensiService.create(absensi));
    } else {
      this.subscribeToSaveResponse(this.absensiService.update(absensi));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IAbsensi | null>): void {
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

  protected updateForm(absensi: IAbsensi): void {
    this.absensi = absensi;
    this.absensiFormService.resetForm(this.editForm, absensi);

    this.pegawaisSharedCollection.update(pegawais =>
      this.pegawaiService.addPegawaiToCollectionIfMissing<IPegawai>(pegawais, absensi.pegawai),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pegawaiService
      .query()
      .pipe(map((res: HttpResponse<IPegawai[]>) => res.body ?? []))
      .pipe(map((pegawais: IPegawai[]) => this.pegawaiService.addPegawaiToCollectionIfMissing<IPegawai>(pegawais, this.absensi?.pegawai)))
      .subscribe((pegawais: IPegawai[]) => this.pegawaisSharedCollection.set(pegawais));
  }
}
