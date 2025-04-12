import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ModuleControllerService } from '../../services/services/module-controller.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProjectModule } from '../../services/models/project-module';
import { Fluid } from 'primeng/fluid';
import { Message } from 'primeng/message';
import { Tag } from 'primeng/tag';
import { Textarea } from 'primeng/textarea';
import { DatePicker } from 'primeng/datepicker';
import { Button } from 'primeng/button';
import { InputNumber } from 'primeng/inputnumber';
import { validateProjectDates } from '../util/validators/ValidateProjectDates';
import { CommonModule } from '@angular/common';
import { InputText } from 'primeng/inputtext';

@Component({
    selector: 'app-module-form',
    standalone: true,
    imports: [Fluid, ReactiveFormsModule, Message, Tag, Textarea, DatePicker, Button, InputNumber, CommonModule, InputText],
    templateUrl: './module-form.component.html',
    styleUrl: './module-form.component.scss'
})
export class ModuleFormComponent implements OnInit {
    moduleForm!: FormGroup;
    loading: boolean = false;
    priority: 'Low' | 'Medium' | 'High' = 'Low';
    prioritySeverity: 'info' | 'warn' | 'danger' = 'info';

    @Input() module: ProjectModule = {};
    @Input() project_id: number | undefined;
    @Input() isOnUpdate: boolean = false;
    @Input() set initUpdateF(trigger: boolean) {
        if (trigger) {
            this.initFormUpdate();
        }
    }

    @Output() hideForm = new EventEmitter<void>();
    @Output() moduleCreated = new EventEmitter<ProjectModule>();

    constructor(private _moduleService: ModuleControllerService) {}

    ngOnInit() {
        this.initForm();
    }

    initForm() {
        this.moduleForm = new FormGroup(
            {
                title: new FormControl('', [Validators.required]),
                priority: new FormControl(1, [Validators.required, Validators.min(1), Validators.max(10)]),
                description: new FormControl('', [Validators.required]),
                beginDateDate: new FormControl(null, [Validators.required]),
                beginDateTime: new FormControl(null, [Validators.required]),
                deadlineDate: new FormControl(null, [Validators.required]),
                deadlineTime: new FormControl(null, [Validators.required])
            },
            { validators: validateProjectDates() }
        );
        this.moduleForm.get('priority')?.valueChanges.subscribe((value) => {
            this.updatePriorityLabel(value);
        });
        this.updatePriorityLabel(this.moduleForm.get('priority')?.value);
    }

    initFormUpdate() {
        const begin = new Date(this.module.beginDate!);
        const deadline = new Date(this.module.deadline!);
        this.moduleForm = new FormGroup(
            {
                title: new FormControl(this.module.title, [Validators.required]),
                priority: new FormControl(this.module.priority, [Validators.required, Validators.min(1), Validators.max(10)]),
                description: new FormControl(this.module.description, [Validators.required]),
                beginDateDate: new FormControl(begin, [Validators.required]),
                beginDateTime: new FormControl(begin, [Validators.required]),
                deadlineDate: new FormControl(deadline, [Validators.required]),
                deadlineTime: new FormControl(deadline, [Validators.required])
            },
            { validators: validateProjectDates() }
        );
        this.moduleForm.get('priority')?.valueChanges.subscribe((value) => {
            this.updatePriorityLabel(value);
        });
        this.updatePriorityLabel(this.moduleForm.get('priority')?.value);
    }

    cancel() {
        this.hideForm.emit();
        this.initForm();
    }

    saveModule() {
        this.setRequest();
        this.loading = true;
        if (!this.isOnUpdate) {
            this._moduleService
                .addModule({
                    project_id: this.project_id!,
                    body: this.module
                })
                .subscribe({
                    next: (response) => {
                        this.moduleCreated.emit();
                        this.initForm();
                        this.module = {};
                    },
                    error: (err) => {
                        // treat errors
                    }
                });
        } else {
            this._moduleService
                .updateModule({
                    module_id: this.module.moduleId!,
                    body: this.module
                })
                .subscribe({
                    next: (response) => {
                        this.moduleCreated.emit(response);
                        this.module = response;
                        this.initFormUpdate();
                    },
                    error: (err) => {
                        // treat errors
                    }
                });
        }

        this.loading = false;
    }

    setRequest() {
        this.module.title = this.moduleForm.get('title')?.value;
        this.module.description = this.moduleForm.get('description')?.value;
        this.module.priority = this.moduleForm.get('priority')?.value;
        if (this.beginDateValue) this.module.beginDate = this.beginDateValue;
        if (this.deadlineValue) this.module.deadline = this.deadlineValue;
    }

    updatePriorityLabel(value: number): void {
        if (value <= 3) {
            this.prioritySeverity = 'info';
            this.priority = 'Low';
        } else if (value <= 7) {
            this.prioritySeverity = 'warn';
            this.priority = 'Medium';
        } else {
            this.prioritySeverity = 'danger';
            this.priority = 'High';
        }
    }

    get beginDateValue() {
        const date = this.moduleForm.get('beginDateDate')?.value;
        const time = this.moduleForm.get('beginDateTime')?.value;

        if (!date || !time) return null;

        return new Date(date.getFullYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds()).toISOString();
    }

    get deadlineValue() {
        const date = this.moduleForm.get('deadlineDate')?.value;
        const time = this.moduleForm.get('deadlineTime')?.value;

        if (!date || !time) return null;

        return new Date(date.getFullYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds()).toISOString();
    }
}
