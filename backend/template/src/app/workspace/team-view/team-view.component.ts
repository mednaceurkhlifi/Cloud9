import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Tag } from 'primeng/tag';
import { Button } from 'primeng/button';
import { DatePicker } from 'primeng/datepicker';
import { FileUpload } from 'primeng/fileupload';
import { Fluid } from 'primeng/fluid';
import { InputNumber } from 'primeng/inputnumber';
import { InputText } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import { CommonModule } from '@angular/common';
import { Select } from 'primeng/select';
import { Textarea } from 'primeng/textarea';
import { User } from '../../services/models/user';
import { ProjectUserProjection } from '../../services/models/project-user-projection';
import { AddMemberFormComponent } from '../add-member-form/add-member-form.component';
import { UserDto } from '../../services/models/user-dto';
import { Task } from '../../services/models/task';
import { Project } from '../../services/models/project';
import { ProjectUserControllerService } from '../../services/services/project-user-controller.service';
import { TaskControllerService } from '../../services/services/task-controller.service';

@Component({
    selector: 'app-team-view',
    standalone: true,
    imports: [Tag, Button, DatePicker, FileUpload, Fluid, CommonModule, InputNumber, InputText, Message, Select, Textarea, AddMemberFormComponent],
    templateUrl: './team-view.component.html',
    styleUrl: './team-view.component.scss'
})
export class TeamViewComponent {
    @Input() usersTask!: User[];
    @Input() team!: ProjectUserProjection[];
    @Input() task!: Task;
    @Input() project!: Project;
    @Input() isAdmin: boolean = false;

    @Output() userAssigned: EventEmitter<User> = new EventEmitter();
    @Output() memberAssigned: EventEmitter<UserDto> = new EventEmitter();

    @Output() deleted: EventEmitter<String> = new EventEmitter();

    constructor(
        private _projectUserService: ProjectUserControllerService,
        private _taskService: TaskControllerService
    ) {}

    memberCreated($event: UserDto) {
        if (this.usersTask) {
            this.userAssigned.emit({
                fullName: $event.fullName,
                email: $event.email,
                image: $event.image
            });
        } else {
            this.memberAssigned.emit($event);
        }
    }

    deleteProjectMember(t: ProjectUserProjection) {
        this._projectUserService
            .deleteProjectUser({
                project_id: this.project.projectId!,
                user_email: t.user?.email!
            })
            .subscribe({
                next: (response) => {
                    this.deleted.emit(t.user?.email!);
                },
                error: (err) => {}
            });
    }

    deleteTaskMember(user: User) {
        this._taskService
            .removeUserFromTask({
                task_id: this.task.taskId!,
                user_email: user.email!
            })
            .subscribe({
                next: (response) => {
                    this.deleted.emit(user.email);
                },
                error: (err) => {}
            });
    }
}
