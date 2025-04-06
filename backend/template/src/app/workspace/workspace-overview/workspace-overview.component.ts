import { Component, OnInit } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { OrganizationChartModule } from 'primeng/organizationchart';
import { Fieldset } from 'primeng/fieldset';
import { InputText } from 'primeng/inputtext';
import { Textarea } from 'primeng/textarea';
import { Fluid } from 'primeng/fluid';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Message } from 'primeng/message';
import { FileUpload } from 'primeng/fileupload';
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
import { Table } from 'primeng/table';

@Component({
    selector: 'app-workspace-overview',
    standalone: true,
    imports: [OrganizationChartModule, Fieldset, InputText, Textarea, Fluid,
        ReactiveFormsModule, Message, FileUpload, ToastModule, CommonModule, ButtonModule,
        TableModule, TagModule, IconFieldModule, InputTextModule, InputIconModule, MultiSelectModule, SelectModule],
    templateUrl: './workspace-overview.component.html',
    styleUrl: './workspace-overview.component.scss'
})
export class WorkspaceOverviewComponent implements OnInit {
    wkForm!: FormGroup;
    image: any = null;
    workspaceResponse!: WorkspaceResponse;
    isWkFound: boolean = false;
    loading: boolean = false;

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
        private _wkService: WorkspaceControllerService
    ) {
    }

    ngOnInit() {
        this._wkService.getWorkspace({
            'organization_id': 1,
            'size': 5,
            'page_no': 0
        }).subscribe({
            next: response => {
                this.isWkFound = true;
                this.workspaceResponse = response;
            },
            error: err => {
                this.wkForm = new FormGroup({
                    name: new FormControl('', [Validators.required]),
                    description: new FormControl('', Validators.required),
                    image: new FormControl('')
                });
                this.isWkFound = false;
            }
        })
    }

    createWorkspace() {}

    clear(table: Table) {
        table.clear();
    }

    getSeverity(status: string): 'success' | 'info' | 'warn' | 'secondary' | 'contrast' | 'danger' | undefined {
        switch (status) {
            case 'unqualified':
                return 'danger';
            case 'qualified':
                return 'success';
            case 'new':
                return 'info';
            case 'negotiation':
                return 'warn';
            case 'renewal':
                return undefined;
            default:
                return undefined;
        }
    }

}
