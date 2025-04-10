import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProjectControllerService } from '../../services/services/project-controller.service';
import { Fluid } from 'primeng/fluid';
import { Message } from 'primeng/message';
import { FileSelectEvent, FileUpload } from 'primeng/fileupload';
import { Button } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { InputText } from 'primeng/inputtext';
import { Textarea } from 'primeng/textarea';
import { Project } from '../../services/models/project';
import { Slider } from 'primeng/slider';
import { InputTextModule } from 'primeng/inputtext';
import { DatePickerModule } from 'primeng/datepicker';
import { InputNumber } from 'primeng/inputnumber';
import { Tag } from 'primeng/tag';
import { Select } from 'primeng/select';
import { validateProjectDates } from '../util/validators/ValidateProjectDates';

@Component({
    selector: 'app-project-form',
    standalone: true,
    imports: [ReactiveFormsModule, Select, Fluid, Message, FileUpload, Button, CommonModule, InputText, Textarea, Slider, InputTextModule, DatePickerModule, FormsModule, InputNumber, Tag],
    templateUrl: './project-form.component.html',
    styleUrl: './project-form.component.scss'
})
export class ProjectFormComponent implements OnInit {
    projectForm!: FormGroup;
    image: any = null;
    loading: boolean = false;
    isImageChanged: boolean = false;
    formTitle: string = 'Create new project';
    priority: 'Low' | 'Medium' | 'High' = 'Low';
    projectStatusList = [
        { name: 'Not started', value: 'NOT_STARTED' },
        { name: 'In progress', value: 'IN_PROGRESS' },
        { name: 'On hold', value: 'ON_HOLD' },
        { name: 'Canceled', value: 'CANCELED' },
        { name: 'Under review', value: 'UNDER_REVIEW' },
        { name: 'Finished', value: 'FINISHED' }
    ];
    selectedStatus: string | null = null;

    @Input() project: Project = {};
    @Input() workspace_id: number | undefined;
    @Input() isOnUpdate: boolean = false;

    @Output() hideForm = new EventEmitter<void>();
    @Output() projectCreated = new EventEmitter<void>();

    prioritySeverity: 'info' | 'warn' | 'danger' = 'info';

    constructor(private _projectService: ProjectControllerService) {}

    ngOnInit() {
        this.projectForm = new FormGroup(
            {
                name: new FormControl('', [Validators.required]),
                priority: new FormControl(1, [Validators.required, Validators.min(1), Validators.max(10)]),
                description: new FormControl('', [Validators.required]),
                beginDateDate: new FormControl(null, [Validators.required]),
                beginDateTime: new FormControl(null, [Validators.required]),
                deadlineDate: new FormControl(null, [Validators.required]),
                deadlineTime: new FormControl(null, [Validators.required])
            },
            { validators: validateProjectDates() }
        );
        this.projectForm.get('priority')?.valueChanges.subscribe((value) => {
            this.updatePriorityLabel(value);
        });
        this.updatePriorityLabel(this.projectForm.get('priority')?.value);
    }

    saveProject() {
        this.setRequest();
        if (!this.isOnUpdate) {
            if(this.workspace_id) {
                this._projectService.addProjectToWorkspace({
                    workspace_id: this.workspace_id,
                    body: {
                        project: this.project,
                        image: this.image ? this.image : undefined
                    }
                }).subscribe({
                    next: response => {

                    },
                    error: err => {

                    }
                });
            }
        } else {
        }
    }

    changeImage(event: FileSelectEvent) {
        this.image = event.files[0];
        this.isImageChanged = true;
    }

    cancel() {
        this.hideForm.emit();
    }

    removeImage() {
        this.image = undefined;
        this.project.image = 'default_project.jpg';
        this.isImageChanged = true;
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

    setRequest() {
        this.project.name = this.projectForm.get('name')?.value;
        this.project.description = this.projectForm.get('description')?.value;
        this.project.priority = this.projectForm.get('priority')?.value;
        if (this.beginDateValue) this.project.beginDate = this.beginDateValue;
        if (this.deadlineValue) this.project.deadline = this.deadlineValue;
        if (!this.isOnUpdate) this.project.status = 'NOT_STARTED';
        // status if on update
    }

    get beginDateValue() {
        const date = this.projectForm.get('beginDateDate')?.value;
        const time = this.projectForm.get('beginDateTime')?.value;

        if (!date || !time) return null;

        return new Date(date.getFullYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds()).toISOString();
    }

    get deadlineValue() {
        const date = this.projectForm.get('deadlineDate')?.value;
        const time = this.projectForm.get('deadlineTime')?.value;

        if (!date || !time) return null;

        return new Date(date.getFullYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds()).toISOString();
    }
}
