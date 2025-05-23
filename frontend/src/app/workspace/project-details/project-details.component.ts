import { Component, OnInit } from '@angular/core';
import { Tag } from 'primeng/tag';
import { ProgressBar } from 'primeng/progressbar';
import { Button } from 'primeng/button';
import { CommonModule, DatePipe, NgIf } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectControllerService } from '../../services/services/project-controller.service';
import { Project } from '../../services/models/project';
import { ModuleControllerService } from '../../services/services/module-controller.service';
import { TaskControllerService } from '../../services/services/task-controller.service';
import { TaskResponse } from '../../services/models/task-response';
import { ModuleResponse } from '../../services/models/module-response';
import { Paginator } from 'primeng/paginator';
import { Dialog } from 'primeng/dialog';
import { ModuleFormComponent } from '../module-form/module-form.component';
import { ProjectFormComponent } from '../project-form/project-form.component';
import { Avatar } from 'primeng/avatar';
import { AvatarGroup } from 'primeng/avatargroup';
import { TaskFormComponent } from '../task-form/task-form.component';
import { TaskDetailsComponent } from '../task-details/task-details.component';
import { DocumentsViewComponent } from '../documents-view/documents-view.component';
import { ProjectUserControllerService } from '../../services/services/project-user-controller.service';
import { ProjectUserProjection } from '../../services/models/project-user-projection';
import { Message } from 'primeng/message';
import { ReactiveFormsModule } from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { UserDto } from '../../services/models/user-dto';
import { AddMemberFormComponent } from '../add-member-form/add-member-form.component';
import { TeamViewComponent } from '../team-view/team-view.component';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { Toast } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ChatChannelComponent } from '../chat/chat-channel/chat-channel.component';
import { TokenService } from '../../token-service/token.service';

@Component({
    selector: 'app-project-details',
    standalone: true,
    imports: [
        Tag,
        ProgressBar,
        Button,
        DatePipe,
        CommonModule,
        TableModule,
        Paginator,
        Dialog,
        ModuleFormComponent,
        ProjectFormComponent,
        Avatar,
        AvatarGroup,
        TaskFormComponent,
        TaskDetailsComponent,
        DocumentsViewComponent,
        Message,
        ReactiveFormsModule,
        InputText,
        AddMemberFormComponent,
        TeamViewComponent,
        ConfirmDialog,
        Toast,
        ChatChannelComponent
    ],
    templateUrl: './project-details.component.html',
    styleUrl: './project-details.component.scss',
    providers: [ConfirmationService, MessageService]
})
export class ProjectDetailsComponent implements OnInit {
    status: string = 'IN_PROGRESS';
    priority: any = 5;
    project: Project = {};
    taskResponse: TaskResponse = {};
    moduleResponse: ModuleResponse = {};
    team: ProjectUserProjection[] = [];
    size_task!: number;
    page_task!: number;
    size_module!: number;
    page_module!: number;
    loading_module: boolean = false;
    loading_task: boolean = false;
    isCreatingModule: boolean = false;
    isCreatingTask: boolean = false;
    isCreatingManager: boolean = false;
    isCreatingMember: boolean = false;
    isOnUpdate: boolean = false;
    projectId!: any;
    userRole: any;
    projectUser!: ProjectUserProjection;
    user_email: string = '';
    isManagerOrAdmin: boolean = false;
    isAdmin: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private _projectService: ProjectControllerService,
        private _moduleService: ModuleControllerService,
        private _taskService: TaskControllerService,
        private _projectUserService: ProjectUserControllerService,
        private router: Router,
        private confirmationService: ConfirmationService,
        private messageService: MessageService,
        private _tokenService: TokenService
    ) {}

    ngOnInit() {
        this.projectId = this.route.snapshot.paramMap.get('project_id');
        this.size_task = 10;
        this.page_task = 0;
        this.size_module = 10;
        this.page_module = 0;
        this.userRole = this._tokenService.getCurrentUserRole();
        this.user_email = this._tokenService.getUserEmail();
        if (this.projectId) {
            this.getProject(parseInt(this.projectId));
            this._projectUserService.getProjectUser({
                user_email: this.user_email,
                project_id: parseInt(this.projectId)
            }).subscribe({
                next: (result) => {
                    this.projectUser = result;
                    if(this.userRole.includes('ADMIN') || this.projectUser.role == 'MANAGER')
                        this.isManagerOrAdmin = true;
                },
                error: err => {}
            });

            if(this.userRole.includes('ADMIN'))
                this.isAdmin = true;
        }
    }

    getProject(projectId: number) {
        this._projectService
            .getProjectById({
                project_id: projectId
            })
            .subscribe({
                next: (response) => {
                    this.project = response;
                    this.getTeam(projectId);
                    this.getProjectTasks(projectId);
                    this.getProjectModules(projectId);
                },
                error: (err) => {
                    // treat errors
                }
            });
    }

    getTeam(projectId: number) {
        this._projectUserService
            .getProjectTeams({
                project_id: projectId
            })
            .subscribe({
                next: (response) => {
                    this.team = response;
                },
                error: (err) => {
                    // treat errors
                }
            });
    }

    getInFrontTeam() {
        return this.team.slice(0, 8);
    }

    getSizeEndTeam() {
        return Math.max(this.team.length - 8, 0);
    }

    getProjectTasks(projectId: number) {
        this.loading_task = true;
        this._taskService
            .getTaskByProject({
                project_id: projectId,
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

    getProjectModules(projectId: number) {
        this.loading_module = true;
        this._moduleService
            .getModulesByProject({
                project_id: projectId,
                size: this.size_module,
                page_no: this.page_module
            })
            .subscribe({
                next: (response) => {
                    this.moduleResponse = response;
                },
                error: (err) => {
                    // treat errors
                }
            });
        this.loading_module = false;
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

    editProject() {
        this.isOnUpdate = true;
    }

    deleteProject() {
        this.confirmationService.confirm({
            message: 'Do you want to delete this project?',
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
                this._projectService
                    .deleteProject({
                        project_id: this.project.projectId!
                    })
                    .subscribe({
                        next: () => {
                            this.router.navigate(['/workspace']);
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

    addModule() {
        this.isCreatingModule = true;
    }

    showModule(moduleId: any) {
        this.router.navigate(['/workspace/module', moduleId]);
    }

    addTask() {
        this.isCreatingTask = true;
    }

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
        this.getProjectTasks(this.project.projectId!);
    }

    onPageChangeModule($event: any) {
        this.page_module = $event.page;
        this.size_module = $event.rows;
        this.getProjectModules(this.project.projectId!);
    }

    moduleCreated() {
        this.getProjectModules(this.project.projectId!);
        this.isCreatingModule = false;
    }

    taskCreated() {
        this.getProjectTasks(this.project.projectId!);
        this.getProject(this.project.projectId!);
        this.isCreatingTask = false;
    }

    showTask(taskId: any) {
        this.router.navigate(['/workspace/task', taskId]);
    }

    checkProjectManagerExist(): boolean {
        if (this.team.length > 0) {
            if (this.team.findIndex((p) => p.role === 'MANAGER') != -1) return true;
        }
        return false;
    }

    getProjectManager(): ProjectUserProjection | undefined {
        if (this.checkProjectManagerExist()) {
            return this.team.find((p) => p.role === 'MANAGER');
        }
        return {};
    }

    checkProjectMemberExist(): boolean {
        if (this.team.length > 0) {
            if (this.team.findIndex((p) => p.role === 'TEAM_MEMBER') != -1) return true;
        }
        return false;
    }

    showAddManagerForm() {
        this.isCreatingManager = true;
    }

    memberCreated(member: UserDto) {
        this.team.push({
            role: member.role,
            project: this.project!,
            user: {
                email: member.email,
                fullName: member.fullName,
                image: member.image
            }
        });
        this.userRole = this._tokenService.getCurrentUserRole();
        if(this.userRole.includes('ADMIN') || this.projectUser.role == 'MANAGER')
            this.isManagerOrAdmin = true;
        this.isCreatingManager = false;
        this.isCreatingMember = false;
    }

    deleteProjectManager() {
        let index = this.team.findIndex((p) => p.role === 'MANAGER');
        this._projectUserService
            .deleteProjectUser({
                project_id: this.project.projectId!,
                user_email: this.team[index].user!.email!
            })
            .subscribe({
                next: (response) => {
                    this.team.splice(index, 1);
                },
                error: (err) => {}
            });
    }

    showAddMemberForm() {
        this.isCreatingMember = true;
    }

    memberDeleted($event: String) {
        let index = this.team.findIndex((p) => p.user?.email === $event);
        this.team.splice(index, 1);
    }

    projectUpdated($event: Project) {
        this.isOnUpdate = false;
        this.project = $event;
    }
}
