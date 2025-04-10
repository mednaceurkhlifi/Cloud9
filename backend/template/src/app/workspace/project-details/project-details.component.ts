import { Component, OnInit } from '@angular/core';
import { Tag } from 'primeng/tag';
import { ProgressBar } from 'primeng/progressbar';
import { Button } from 'primeng/button';
import { DatePipe, NgIf } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectControllerService } from '../../services/services/project-controller.service';
import { Project } from '../../services/models/project';
import { ModuleControllerService } from '../../services/services/module-controller.service';
import { TaskControllerService } from '../../services/services/task-controller.service';
import { ProjectDocumentControllerService } from '../../services/services/project-document-controller.service';
import { TaskResponse } from '../../services/models/task-response';
import { ModuleResponse } from '../../services/models/module-response';
import { Paginator } from 'primeng/paginator';
import { Dialog } from 'primeng/dialog';
import { ModuleFormComponent } from '../module-form/module-form.component';
import { ProjectFormComponent } from '../project-form/project-form.component';
import { Avatar } from 'primeng/avatar';
import { AvatarGroup } from 'primeng/avatargroup';

@Component({
    selector: 'app-project-details',
    standalone: true,
    imports: [Tag, ProgressBar, Button, DatePipe, NgIf, TableModule, Paginator, Dialog, ModuleFormComponent, ProjectFormComponent, Avatar, AvatarGroup],
    templateUrl: './project-details.component.html',
    styleUrl: './project-details.component.scss'
})
export class ProjectDetailsComponent implements OnInit {
    status: string = 'IN_PROGRESS';
    priority: any = 5;
    project: Project = {};
    taskResponse: TaskResponse = {};
    moduleResponse: ModuleResponse = {};
    size_task!: number;
    page_task!: number;
    size_module!: number;
    page_module!: number;
    loading_module: boolean = false;
    loading_task: boolean = false;
    isCreatingModule: boolean = false;
    isCreatingTask: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private _projectService: ProjectControllerService,
        private _moduleService: ModuleControllerService,
        private _taskService: TaskControllerService,
        private _documentService: ProjectDocumentControllerService,
        private router: Router
    ) {}

    ngOnInit() {
        const projectId = this.route.snapshot.paramMap.get('project_id');
        this.size_task = 10;
        this.page_task = 0;
        this.size_module = 10;
        this.page_module = 0;
        if (projectId) {
            this.getProject(parseInt(projectId));
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
                    this.getProjectTasks(projectId);
                    this.getProjectModules(projectId);
                },
                error: (err) => {
                    // treat errors
                }
            });
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

    editProduct() {}

    deleteProduct() {}

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
}
