import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProjectUser } from '../../services/models/project-user';
import { ProjectUserControllerService } from '../../services/services/project-user-controller.service';
import { Project } from '../../services/models/project';
import { UserDto } from '../../services/models/user-dto';
import { Message } from 'primeng/message';
import { Button } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { InputText } from 'primeng/inputtext';
import { TaskControllerService } from '../../services/services/task-controller.service';
import { Task } from '../../services/models/task';

@Component({
    selector: 'app-add-member-form',
    standalone: true,
    imports: [ReactiveFormsModule, Message, Button, CommonModule, InputText],
    templateUrl: './add-member-form.component.html',
    styleUrl: './add-member-form.component.scss'
})
export class AddMemberFormComponent implements OnInit {
    addMemForm!: FormGroup;
    @Input() project!: Project;
    @Input() task!: Task;
    @Input() isCreatingManager: boolean = false;
    @Input() taskMember: boolean = false;

    @Output() memberCreated = new EventEmitter<UserDto>();

    constructor(
        private _projectUserService: ProjectUserControllerService,
        private _taskService : TaskControllerService
    ) {}

    ngOnInit() {
        this.addMemForm = new FormGroup({
            email: new FormControl('', [Validators.required, Validators.email])
        });
    }

    addManager() {
        if(!this.taskMember) {
            if (this.isCreatingManager) {
                this.createManager();
            } else {
                this.createMember();
            }
        } else {
            this.addMemberToTask();
        }


    }

    private createManager() {
        let manager: ProjectUser = {
            role: 'MANAGER',
            user: { email: this.addMemForm.get('email')?.value },
            project: this.project
        };
        this._projectUserService
            .addProjectManager({
                body: manager
            })
            .subscribe({
                next: (response) => {
                    this.memberCreated.emit(response);
                    this.ngOnInit();                    },
                error: (err) => {
                    // treat errors
                }
            });
    }
    private createMember() {
        let manager: ProjectUser = {
            role: 'TEAM_MEMBER',
            user: { email: this.addMemForm.get('email')?.value },
            project: this.project
        };
        this._projectUserService
            .addProjectMember({
                project_id: this.project.projectId!,
                body: manager
            })
            .subscribe({
                next: (response) => {
                    this.memberCreated.emit(response);
                    this.ngOnInit();
                },
                error: (err) => {
                    // treat errors
                }
            });
    }

    private addMemberToTask() {
        this._taskService.assignUserToTask({
            task_id: this.task.taskId!,
            user_email: this.addMemForm.get('email')?.value
        }).subscribe({
            next: (response) => {
                this.memberCreated.emit(response);
                this.ngOnInit();
            },
            error: err => {

            }
        });
    }
}
