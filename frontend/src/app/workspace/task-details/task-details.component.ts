import { Component, Input, OnInit } from '@angular/core';
import { Task } from '../../services/models/task';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskControllerService } from '../../services/services/task-controller.service';
import { ProjectDocumentControllerService } from '../../services/services/project-document-controller.service';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { Toast } from 'primeng/toast';
import { Button } from 'primeng/button';
import { Avatar } from 'primeng/avatar';
import { AvatarGroup } from 'primeng/avatargroup';
import { CommonModule, DatePipe } from '@angular/common';
import { Dialog } from 'primeng/dialog';
import { ModuleFormComponent } from '../module-form/module-form.component';
import { ProgressBar } from 'primeng/progressbar';
import { Tag } from 'primeng/tag';
import { TaskFormComponent } from '../task-form/task-form.component';
import { TeamViewComponent } from '../team-view/team-view.component';
import { TableModule } from 'primeng/table';
import { DocumentsViewComponent } from '../documents-view/documents-view.component';
import { ProjectDocument } from '../../services/models/project-document';
import { User } from '../../services/models/user';
import { ChatChannelComponent } from '../chat/chat-channel/chat-channel.component';
import { TokenService } from '../../token-service/token.service';
import { ProjectUserControllerService } from '../../services/services/project-user-controller.service';
import { ProjectUserProjection } from '../../services/models/project-user-projection';

@Component({
    selector: 'app-task-details',
    standalone: true,
    imports: [ConfirmDialog, Toast, Button, Avatar, AvatarGroup, CommonModule, DatePipe, Dialog, ModuleFormComponent, ProgressBar, Tag, TaskFormComponent, TeamViewComponent, TableModule, DocumentsViewComponent, ChatChannelComponent],
    templateUrl: './task-details.component.html',
    styleUrl: './task-details.component.scss',
    providers: [ConfirmationService, MessageService]
})
export class TaskDetailsComponent implements OnInit {
    task!: Task;
    isOnUpdate: boolean = false;
    priority: 'Low' | 'Medium' | 'High' = 'Low';
    isShowMember: boolean = false;
    loading: boolean = false;
    isManagerOrAdmin: boolean = false;
    isAdmin: boolean = false;
    user_email: string = '';
    userRole: any;
    projectUser!: ProjectUserProjection;

    constructor(
        private _taskService: TaskControllerService,
        private _documentService: ProjectDocumentControllerService,
        private route: ActivatedRoute,
        private router: Router,
        private confirmationService: ConfirmationService,
        private _tokenService: TokenService,
        private messageService: MessageService,
        private _projectUserService: ProjectUserControllerService
    ) {}

    ngOnInit() {
        const taskId = this.route.snapshot.paramMap.get('task_id');
        this.userRole = this._tokenService.getCurrentUserRole();
        this.user_email = this._tokenService.getUserEmail();
        if (taskId) {
            this.getTask(parseInt(taskId));
        }
    }

    getTask(taskId: number) {
        this.loading = true;
        this._taskService
            .getTaskById({
                task_id: taskId
            })
            .subscribe({
                next: (response) => {
                    this.task = response;
                    let projectId = 0;
                    if(response.project?.projectId)
                        projectId = response.project.projectId;
                    else
                        projectId = response.module?.project?.projectId!;
                    this._projectUserService.getProjectUser({
                        user_email: this.user_email,
                        project_id: projectId
                    }).subscribe({
                        next: (result) => {
                            this.projectUser = result
                            if(this.userRole.includes('ADMIN') || this.projectUser.role == 'MANAGER')
                                this.isManagerOrAdmin = true;
                        },
                        error: err => {}
                    });
                    if(this.userRole.includes('ADMIN'))
                        this.isAdmin = true;
                },
                error: (err) => {
                    // treat errors
                }
            });
        this.loading = false;
    }

    goBack() {
        if (this.task.project) this.router.navigate(['/workspace/project', this.task.project.projectId]);
        else this.router.navigate(['/workspace/module', this.task.module?.moduleId]);
    }

    taskUpdated($event: Task) {
        this.isOnUpdate = false;
        this.task = $event;
    }

    editTask() {
        this.isOnUpdate = true;
    }

    deleteTask() {
        this.confirmationService.confirm({
            message: 'Do you want to delete this task?',
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
                this._taskService
                    .deleteTask({
                        task_id: this.task.taskId!
                    })
                    .subscribe({
                        next: () => {
                            this.goBack();
                        },
                        error: (err) => {
                            // treat errors
                        }
                    });
            },
            reject: () => {
                this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected' });
            }
        });
    }

    getSeverityStatus(status: any): 'success' | 'info' | 'warn' | 'secondary' | 'contrast' | 'danger' | undefined {
        switch (status) {
            case 'NOT_STARTED':
                return 'secondary';
            case 'IN_PROGRESS':
                return 'info';
            case 'ON_HOLD':
                return 'secondary';
            case 'CANCELED':
                return 'danger';
            case 'UNDER_REVIEW':
                return 'warn';
            case 'FINISHED':
                return 'success';
            default:
                return undefined;
        }
    }

    getSeverityPriority(priority: any): 'info' | 'warn' | 'danger' | undefined {
        if (priority <= 3) return 'info';
        else if (priority <= 7) return 'warn';
        else if (priority <= 10) return 'danger';
        return undefined;
    }

    getInFrontTeam() {
        return this.task.members!.slice(0, 8);
    }

    getSizeEndTeam() {
        return Math.max(this.task.members!.length - 8, 0);
    }

    showMember() {
        this.isShowMember = true;
    }

    userAssigned($event: User) {
        this.task.members?.push($event);
    }

    userDeleted($event: String) {
        if (this.task.members) {
            let index = this.task.members.findIndex((u) => u.email == $event);
            if (index > -1) {
                this.task.members!.splice(index, 1);
            }
        }

        this.isShowMember = false;
    }
}
