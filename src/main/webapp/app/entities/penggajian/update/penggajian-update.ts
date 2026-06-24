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
import { IPenggajian } from '../penggajian.model';
import { PenggajianService } from '../service/penggajian.service';

import { PenggajianFormGroup, PenggajianFormService } from './penggajian-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-penggajian-update',
  templateUrl: './penggajian-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class PenggajianUpdate implements OnInit {
  readonly isSaving = signal(false);
  penggajian: IPenggajian | null = null;

  pegawaisSharedCollection = signal<IPegawai[]>([]);

  protected penggajianService = inject(PenggajianService);
  protected penggajianFormService = inject(PenggajianFormService);
  protected pegawaiService = inject(PegawaiService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PenggajianFormGroup = this.penggajianFormService.createPenggajianFormGroup();

  comparePegawai = (o1: IPegawai | null, o2: IPegawai | null): boolean => this.pegawaiService.comparePegawai(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ penggajian }) => {
      this.penggajian = penggajian;
      if (penggajian) {
        this.updateForm(penggajian);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const penggajian = this.penggajianFormService.getPenggajian(this.editForm);
    if (penggajian.id === null) {
      this.subscribeToSaveResponse(this.penggajianService.create(penggajian));
    } else {
      this.subscribeToSaveResponse(this.penggajianService.update(penggajian));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPenggajian | null>): void {
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

  protected updateForm(penggajian: IPenggajian): void {
    this.penggajian = penggajian;
    this.penggajianFormService.resetForm(this.editForm, penggajian);

    this.pegawaisSharedCollection.update(pegawais =>
      this.pegawaiService.addPegawaiToCollectionIfMissing<IPegawai>(pegawais, penggajian.pegawai),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pegawaiService
      .query()
      .pipe(map((res: HttpResponse<IPegawai[]>) => res.body ?? []))
      .pipe(
        map((pegawais: IPegawai[]) => this.pegawaiService.addPegawaiToCollectionIfMissing<IPegawai>(pegawais, this.penggajian?.pegawai)),
      )
      .subscribe((pegawais: IPegawai[]) => this.pegawaisSharedCollection.set(pegawais));
  }
}
