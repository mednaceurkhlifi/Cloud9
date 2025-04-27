import { Component, OnInit } from '@angular/core';
import { MeetPlanComponent } from '../meeting/meet-plan/meet-plan.component';
import { TaskResponse } from '../../services/models/task-response';
import { TaskControllerService } from '../../services/services/task-controller.service';
import { Router } from '@angular/router';
import { Button } from 'primeng/button';
import { DatePipe, NgIf } from '@angular/common';
import { Dialog } from 'primeng/dialog';
import { Paginator } from 'primeng/paginator';
import { TableModule } from 'primeng/table';
import { Tag } from 'primeng/tag';
import { TaskFormComponent } from '../task-form/task-form.component';
import { TokenService } from '../../token-service/token.service';
import { ProjectUserProjection } from '../../services/models/project-user-projection';
import { ProjectUserControllerService } from '../../services/services/project-user-controller.service';

@Component({
    selector: 'app-profile',
    standalone: true,
    imports: [MeetPlanComponent, Button, DatePipe, Dialog, Paginator, TableModule, Tag, TaskFormComponent, NgIf],
    templateUrl: './profile.component.html',
    styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
    size_task: number = 10;
    page_task: number = 0;
    taskResponse: TaskResponse = {};
    loading_task: boolean = false;
    user_email!: string;
    projects: ProjectUserProjection[] = [];

    constructor(
        private _taskService: TaskControllerService,
        private router: Router,
        private _tokenService: TokenService,
        private _projectUserService: ProjectUserControllerService
    ) {}

    ngOnInit() {
        this.user_email = this._tokenService.getUserEmail()!;
        this.getProjects();
        this.getTasks();
    }

    getProjects() {
        this._projectUserService.getUserProjectsByEmail({
            user_email: this.user_email
        }).subscribe({
            next: value => {
                this.projects = value;
            },
            error: err => {}
        });
    }
    getSeverityPriority(priority: any): 'info' | 'warn' | 'danger' | undefined {
        if (priority <= 3) return 'info';
        else if (priority <= 7) return 'warn';
        else if (priority <= 10) return 'danger';
        return undefined;
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
        this.getTasks();
    }

    showTask(taskId: any) {
        this.router.navigate(['/workspace/task', taskId]);
    }

    private getTasks() {
        this.loading_task = true;
        this._taskService
            .getTasksByUserEmail({
                user_email: this.user_email,
                size: this.size_task,
                page_no: this.page_task
            })
            .subscribe({
                next: (response) => {
                    this.taskResponse = response;
                },
                error: (err) => {}
            });
        this.loading_task = false;
    }

    showProject(projectId: number) {
        this.router.navigate(['/workspace/project', projectId]);
    }

    getSeverityStatus(status: string): 'success' | 'info' | 'warn' | 'secondary' | 'contrast' | 'danger' | undefined {
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

}
