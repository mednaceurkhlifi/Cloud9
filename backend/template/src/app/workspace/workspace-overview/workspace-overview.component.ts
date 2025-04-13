import { Component, OnInit } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { OrganizationChartModule } from 'primeng/organizationchart';
import { Fieldset } from 'primeng/fieldset';
import { InputText } from 'primeng/inputtext';
import { Textarea } from 'primeng/textarea';
import { Fluid } from 'primeng/fluid';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Message } from 'primeng/message';
import { FileSelectEvent, FileUpload } from 'primeng/fileupload';
import { ToastModule } from 'primeng/toast';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { WorkspaceControllerService } from '../../services/services/workspace-controller.service';
import { WorkspaceResponse } from '../../services/models/workspace-response';
import { InputTextModule } from 'primeng/inputtext';
import { MultiSelectModule } from 'primeng/multiselect';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { Workspace } from '../../services/models/workspace';
import { Router } from '@angular/router';
import { WorkspaceRequest } from '../../services/models/workspace-request';
import { ProjectFormComponent } from '../project-form/project-form.component';

@Component({
    selector: 'app-workspace-overview',
    standalone: true,
    imports: [
        OrganizationChartModule,
        Fieldset,
        InputText,
        Textarea,
        Fluid,
        ReactiveFormsModule,
        Message,
        FileUpload,
        ToastModule,
        CommonModule,
        ButtonModule,
        TableModule,
        TagModule,
        IconFieldModule,
        InputTextModule,
        InputIconModule,
        MultiSelectModule,
        SelectModule,
        ConfirmDialog,
        ProjectFormComponent
    ],
    templateUrl: './workspace-overview.component.html',
    styleUrl: './workspace-overview.component.scss',
    providers: [ConfirmationService, MessageService]
})
export class WorkspaceOverviewComponent implements OnInit {
    wkForm!: FormGroup;
    image: any = null;
    workspaceResponse!: WorkspaceResponse;
    isWkFound: boolean = false;
    loading: boolean = false;
    organization_id: number = 2;
    isOnUpdate: boolean = false;
    isImageChanged: boolean = false;
    formTitle: string = 'Create yours now !';
    createProject: boolean = false;

    data: TreeNode[] = [
        {
            label: 'Project',
            expanded: true,
            children: [
                {
                    label: 'Tasks',
                    expanded: true,
                    children: [
                        {
                            label: 'Team'
                        },
                        {
                            label: 'Documents',
                            expanded: true,
                            children: [
                                {
                                    label: 'Images'
                                },
                                {
                                    label: 'Other files'
                                }
                            ]
                        }
                    ]
                },
                {
                    label: 'Modules',
                    expanded: true,
                    children: [
                        {
                            label: 'Tasks',
                            expanded: true,
                            children: [
                                {
                                    label: 'Team'
                                }
                            ]
                        },
                        {
                            label: 'Documents',
                            expanded: true,
                            children: [
                                {
                                    label: 'Images'
                                },
                                {
                                    label: 'Other files'
                                }
                            ]
                        }
                    ]
                },
                {
                    label: 'Team',
                    expanded: true,
                    children: [
                        {
                            label: 'One team manager'
                        },
                        {
                            label: 'Members'
                        }
                    ]
                }
            ]
        }
    ];

    constructor(
        private _wkService: WorkspaceControllerService,
        private router: Router,
        private confirmationService: ConfirmationService,
        private messageService: MessageService
    ) {}

    ngOnInit() {
        this.getWorkspace();
    }

    getWorkspace() {
        this._wkService
            .getWorkspace({
                organization_id: this.organization_id,
                size: 45,
                page_no: 0
            })
            .subscribe({
                next: (response) => {
                    this.workspaceResponse = response;
                    this.wkForm = new FormGroup({
                        name: new FormControl(this.workspaceResponse.projection?.name, [Validators.required]),
                        description: new FormControl(this.workspaceResponse.projection?.description, Validators.required)
                    });
                    this.formTitle = 'Sometimes we need some changes !';
                    this.isWkFound = true;
                },
                error: (err) => {
                    this.wkForm = new FormGroup({
                        name: new FormControl('', [Validators.required]),
                        description: new FormControl('', Validators.required)
                    });
                    this.formTitle = 'Create yours now !';
                    this.isWkFound = false;
                }
            });
    }

    createWorkspace() {
        if (!this.isOnUpdate) {
            this._wkService
                .createWorkspace({
                    body: {
                        workspace: this.setWorkspaceRequest(),
                        image: this.image ? this.image : undefined
                    }
                })
                .subscribe({
                    next: (response) => {
                        this.getWorkspace();
                    },
                    error: (err) => {}
                });
        } else {
            this._wkService
                .updateWorkspace({
                    workspace_id: this.workspaceResponse.projection?.workspaceId!,
                    body: {
                        request: this.setWorkspaceUpdateRequest(),
                        image: this.image ? this.image : undefined
                    }
                })
                .subscribe({
                    next: (response) => {
                        this.convertWorkspaceProjection(response);
                        this.isOnUpdate = false;
                        this.isImageChanged = false;
                    },
                    error: (err) => {}
                });
        }
    }

    setWorkspaceRequest(): Workspace {
        let wk: Workspace = {};
        wk.name = this.wkForm.get('name')?.value;
        wk.description = this.wkForm.get('description')?.value;
        wk.organization = { organizationId: this.organization_id };
        return wk;
    }

    setWorkspaceUpdateRequest(): WorkspaceRequest {
        let wk: WorkspaceRequest = {};

        if (this.wkForm.get('name')?.value != this.workspaceResponse.projection?.name) wk.name = this.wkForm.get('name')?.value;
        if (this.wkForm.get('description')?.value != this.workspaceResponse.projection?.description) wk.description = this.wkForm.get('description')?.value;
        wk.imageOnUpdate = this.isImageChanged;
        return wk;
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

    getSeverityPriority(priority: number): 'info' | 'warn' | 'danger' | undefined {
        if (priority <= 3) return 'info';
        else if (priority <= 7) return 'warn';
        else if (priority <= 10) return 'danger';
        return undefined;
    }

    deleteWorkspace(workspaceId: number | undefined) {
        if (workspaceId) {
            this.confirmationService.confirm({
                message: 'Do you want to delete this workspace?',
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
                    this._wkService
                        .deleteWorkspace({
                            workspace_id: workspaceId
                        })
                        .subscribe({
                            next: () => {
                                this.getWorkspace();
                            }
                        });
                },
                reject: () => {
                    this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected' });
                }
            });
        }
    }

    showUpdateWorkspace() {
        this.isOnUpdate = true;
    }

    convertWorkspaceProjection(workspace: Workspace) {
        this.workspaceResponse.projection = workspace;
    }

    removeImage() {
        this.image = undefined;
        this.workspaceResponse.projection!.image = 'default_workspace.jpg';
        this.isImageChanged = true;
    }

    changeImage(event: FileSelectEvent) {
        this.image = event.files[0];
        this.isImageChanged = true;
    }

    cancelUpdate() {
        this.isOnUpdate = false;
    }

    addProject() {
        this.createProject = true;
    }

    projectCreated() {
        this.getWorkspace();
        this.createProject = false;
    }

    showProject(projectId: number) {
        this.router.navigate(['/workspace/project', projectId]);
    }
}
