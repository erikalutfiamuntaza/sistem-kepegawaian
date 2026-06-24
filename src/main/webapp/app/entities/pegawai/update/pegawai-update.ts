import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IPegawai } from '../pegawai.model';
import { PegawaiService } from '../service/pegawai.service';

import { PegawaiFormGroup, PegawaiFormService } from './pegawai-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-pegawai-update',
  templateUrl: './pegawai-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PegawaiUpdate implements OnInit {
  readonly isSaving = signal(false);
  pegawai: IPegawai | null = null;

  protected pegawaiService = inject(PegawaiService);
  protected pegawaiFormService = inject(PegawaiFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PegawaiFormGroup = this.pegawaiFormService.createPegawaiFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pegawai }) => {
      this.pegawai = pegawai;
      if (pegawai) {
        this.updateForm(pegawai);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const pegawai = this.pegawaiFormService.getPegawai(this.editForm);
    if (pegawai.id === null) {
      this.subscribeToSaveResponse(this.pegawaiService.create(pegawai));
    } else {
      this.subscribeToSaveResponse(this.pegawaiService.update(pegawai));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPegawai | null>): void {
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

  protected updateForm(pegawai: IPegawai): void {
    this.pegawai = pegawai;
    this.pegawaiFormService.resetForm(this.editForm, pegawai);
  }
}
