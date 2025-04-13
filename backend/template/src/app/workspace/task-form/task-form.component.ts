import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Task } from '../../services/models/task';
import { TaskControllerService } from '../../services/services/task-controller.service';
import { validateProjectDates } from '../util/validators/ValidateProjectDates';
import { Button } from 'primeng/button';
import { DatePicker } from 'primeng/datepicker';
import { Fluid } from 'primeng/fluid';
import { InputNumber } from 'primeng/inputnumber';
import { InputText } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import { NgIf } from '@angular/common';
import { Tag } from 'primeng/tag';
import { Textarea } from 'primeng/textarea';
import { FileUpload } from 'primeng/fileupload';
import { Select } from 'primeng/select';

@Component({
    selector: 'app-task-form',
    standalone: true,
    imports: [Button, DatePicker, Fluid, FormsModule, InputNumber, InputText, Message, NgIf, ReactiveFormsModule, Tag, Textarea, FileUpload, Select],
    templateUrl: './task-form.component.html',
    styleUrl: './task-form.component.scss'
})
export class TaskFormComponent implements OnInit {
    taskForm!: FormGroup;
    loading: boolean = false;
    priority: 'Low' | 'Medium' | 'High' = 'Low';
    prioritySeverity: 'info' | 'warn' | 'danger' = 'info';
    projectStatusList = [
        { name: 'To do', value: 'TO_DO' },
        { name: 'In progress', value: 'IN_PROGRESS' },
        { name: 'Blocked', value: 'BLOCKED' },
        { name: 'Rework needed', value: 'REWORK_NEEDED' },
        { name: 'Done', value: 'DONE' },
        { name: 'Archived', value: 'ARCHIVED' }
    ];
    selectedStatus: string | null = null;

    @Input() task: Task = {};
    @Input() project_id: number | undefined;
    @Input() module_id: number | undefined;
    @Input() isOnUpdate: boolean = false;
    @Input() set initUpdateF(trigger: boolean) {
        if (trigger) {
            this.initFormUpdate();
        }
    }

    @Output() hideForm = new EventEmitter<void>();
    @Output() taskCreated = new EventEmitter<Task>();

    constructor(private _taskService: TaskControllerService) {}

    ngOnInit() {
        this.initForm();
    }

    initForm() {
        this.taskForm = new FormGroup(
            {
                title: new FormControl('', [Validators.required]),
                priority: new FormControl(1, [Validators.required, Validators.min(1), Validators.max(10)]),
                description: new FormControl('', [Validators.required]),
                beginDateDate: new FormControl(null, [Validators.required]),
                beginDateTime: new FormControl(null, [Validators.required]),
                deadlineDate: new FormControl(null, [Validators.required]),
                deadlineTime: new FormControl(null, [Validators.required]),
                taskStatus: new FormControl(null, [Validators.required])
            },
            { validators: validateProjectDates() }
        );
        this.taskForm.get('priority')?.valueChanges.subscribe((value) => {
            this.updatePriorityLabel(value);
        });
        this.updatePriorityLabel(this.taskForm.get('priority')?.value);
    }

    initFormUpdate() {
        const begin = new Date(this.task.beginDate!);
        const deadline = new Date(this.task.deadline!);
        this.taskForm = new FormGroup(
            {
                title: new FormControl(this.task.title, [Validators.required]),
                priority: new FormControl(this.task.priority, [Validators.required, Validators.min(1), Validators.max(10)]),
                description: new FormControl(this.task.description, [Validators.required]),
                beginDateDate: new FormControl(begin, [Validators.required]),
                beginDateTime: new FormControl(begin, [Validators.required]),
                deadlineDate: new FormControl(deadline, [Validators.required]),
                deadlineTime: new FormControl(deadline, [Validators.required]),
                taskStatus: new FormControl(this.task.status, [Validators.required])
            },
            { validators: validateProjectDates() }
        );
        this.taskForm.get('priority')?.valueChanges.subscribe((value) => {
            this.updatePriorityLabel(value);
        });
        this.updatePriorityLabel(this.taskForm.get('priority')?.value);
    }

    saveTask() {
        this.setRequest();
        this.loading = true;
        if (!this.isOnUpdate) {
            if(this.project_id) {
                this._taskService
                    .addTaskToProject({
                        project_id: this.project_id!,
                        body: {
                            task: this.task
                        }
                    })
                    .subscribe({
                        next: (response) => {
                            this.taskCreated.emit();
                            this.initForm();
                            this.task = {};
                        },
                        error: (err) => {
                            // treat errors
                        }
                    });
            } else {
                this._taskService
                    .addTaskToModule({
                        module_id: this.module_id!,
                        body: {
                            task: this.task
                        }
                    })
                    .subscribe({
                        next: (response) => {
                            this.taskCreated.emit();
                            this.initForm();
                            this.task = {};
                        },
                        error: (err) => {
                            // treat errors
                        }
                    });
            }
        } else {
            this._taskService
                .updateTask({
                    task_id: this.task.taskId!,
                    body: this.task
                })
                .subscribe({
                    next: (response) => {
                        this.taskCreated.emit(response);
                        this.task = response;
                        this.initForm();
                    },
                    error: (err) => {
                        // treat errors
                    }
                });
        }
        this.loading = false;
    }

    setRequest() {
        this.task.title = this.taskForm.get('title')?.value;
        this.task.description = this.taskForm.get('description')?.value;
        this.task.priority = this.taskForm.get('priority')?.value;
        this.task.status = this.taskForm.get('taskStatus')?.value;
        if (this.beginDateValue) this.task.beginDate = this.beginDateValue;
        if (this.deadlineValue) this.task.deadline = this.deadlineValue;
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
        const date = this.taskForm.get('beginDateDate')?.value;
        const time = this.taskForm.get('beginDateTime')?.value;

        if (!date || !time) return null;

        return new Date(date.getFullYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds()).toISOString();
    }

    get deadlineValue() {
        const date = this.taskForm.get('deadlineDate')?.value;
        const time = this.taskForm.get('deadlineTime')?.value;

        if (!date || !time) return null;

        return new Date(date.getFullYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds()).toISOString();
    }

    cancel() {
        this.hideForm.emit();
        this.initForm();
    }
}
