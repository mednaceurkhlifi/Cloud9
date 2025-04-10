import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ModuleControllerService } from '../../services/services/module-controller.service';
import { ProjectDocumentControllerService } from '../../services/services/project-document-controller.service';
import { ProjectModuleProjection } from '../../services/models/project-module-projection';
import { CommonModule } from '@angular/common';
import { Button } from 'primeng/button';
import { ProgressBar } from 'primeng/progressbar';
import { Tag } from 'primeng/tag';
import { Avatar } from 'primeng/avatar';
import { AvatarGroup } from 'primeng/avatargroup';
import { Dialog } from 'primeng/dialog';
import { Paginator } from 'primeng/paginator';
import { TableModule } from 'primeng/table';
import { TaskResponse } from '../../services/models/task-response';
import { TaskControllerService } from '../../services/services/task-controller.service';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { Toast } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ModuleFormComponent } from '../module-form/module-form.component';
import { ProjectModule } from '../../services/models/project-module';

@Component({
    selector: 'app-module-details',
    standalone: true,
    imports: [CommonModule, Button, ProgressBar, Tag, Avatar, AvatarGroup, Dialog, Paginator, TableModule, ConfirmDialog, Toast, ModuleFormComponent],
    templateUrl: './module-details.component.html',
    styleUrl: './module-details.component.scss',
    providers: [ConfirmationService, MessageService]
})
export class ModuleDetailsComponent implements OnInit {
    module: ProjectModuleProjection = {};
    taskResponse: TaskResponse = {};
    size_task!: number;
    page_task!: number;
    loading_task: boolean = false;
    isCreatingTask: boolean = false;
    isOnUpdate: boolean = false;
    loading: boolean = false;
    priority: 'Low' | 'Medium' | 'High' = 'Low';

    constructor(
        private route: ActivatedRoute,
        private _moduleService: ModuleControllerService,
        private _taskService: TaskControllerService,
        private _documentService: ProjectDocumentControllerService,
        private router: Router,
        private confirmationService: ConfirmationService,
        private messageService: MessageService
    ) {}

    ngOnInit() {
        const moduleId = this.route.snapshot.paramMap.get('module_id');
        this.size_task = 10;
        this.page_task = 0;
        if (moduleId) {
            this.getModule(parseInt(moduleId));
        }
    }

    deleteModule() {
        this.confirmationService.confirm({
            message: 'Do you want to delete this module?',
            header: 'Danger Zone',
            icon: 'pi pi-info-circle',
            rejectLabel: 'Cancel',
            rejectButtonProps: {
                label: 'Cancel',
                severity: 'secondary',
                outlined: true
            },
            acceptButtonProps: {
                label: 'Delete',
                severity: 'danger'
            },

            accept: () => {
                this._moduleService
                    .deleteModule({
                        module_id: this.module.moduleId!
                    })
                    .subscribe({
                        next: () => {
                            this.router.navigate(['/workspace/project', this.module.project?.projectId]);
                        }
                    });
            },
            reject: () => {
                this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected' });
            }
        });
    }

    getModule(moduleId: number) {
        this._moduleService
            .getModuleById({
                module_id: moduleId
            })
            .subscribe({
                next: (response) => {
                    this.module = response;
                    this. updatePriorityLabel(this.module.priority!);
                    this.getModuleTasks(this.module.moduleId!);
                },
                error: (err) => {
                    // treat errors
                }
            });
    }

    goBack() {
        this.router.navigate(['/workspace/project', this.module.project?.projectId]);
    }

    getSeverityPriority(priority: any): 'info' | 'warn' | 'danger' | undefined {
        if (priority <= 3) return 'info';
        else if (priority <= 7) return 'warn';
        else if (priority <= 10) return 'danger';
        return undefined;
    }

    editModule() {
        this.isOnUpdate = true;
    }

    addTask() {}

    showTask(taskId: any) {}

    getSeverityStatusTask(status: any): 'success' | 'info' | 'warn' | 'secondary' | 'contrast' | 'danger' | undefined {
        switch (status) {
            case 'TO_DO':
                return 'secondary';
            case 'IN_PROGRESS':
                return 'info';
            case 'REWORK_NEEDED':
                return 'warn';
            case 'BLOCKED':
                return 'danger';
            case 'UNDER_REVIEW':
                return 'warn';
            case 'ARCHIVED':
                return 'warn';
            case 'DONE':
                return 'success';
            default:
                return undefined;
        }
    }

    onPageChangeTask($event: any) {
        this.page_task = $event.page;
        this.size_task = $event.rows;
        this.getModuleTasks(this.module.moduleId!);
    }

    getModuleTasks(moduleId: number) {
        this.loading_task = true;
        this._taskService
            .getTasksByModule({
                module_id: moduleId,
                size: this.size_task,
                page_no: this.page_task
            })
            .subscribe({
                next: (response) => {
                    this.taskResponse = response;
                },
                error: (err) => {
                    // treat errors
                }
            });
        this.loading_task = false;
    }

    moduleUpdated(m : ProjectModule) {
        this.module = m;
        this. updatePriorityLabel(this.module.priority!);
        this.isOnUpdate = false;
    }

    updatePriorityLabel(value: number): void {
        if (value <= 3) {
            this.priority = 'Low';
        } else if (value <= 7) {
            this.priority = 'Medium';
        } else {
            this.priority = 'High';
        }
    }
}
