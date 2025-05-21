import { Component, Input, OnInit } from '@angular/core';
import { Button } from 'primeng/button';
import { ProjectDocumentControllerService } from '../../services/services/project-document-controller.service';
import { ProjectDocument } from '../../services/models/project-document';
import { CommonModule } from '@angular/common';
import { Dialog } from 'primeng/dialog';
import { FileUpload } from 'primeng/fileupload';
import { ToastModule } from 'primeng/toast';
import { DocumentRequest } from '../../services/models/document-request';
import { ProgressBar } from 'primeng/progressbar';
import { AddDocumentsToTask$Params } from '../../services/fn/project-document-controller/add-documents-to-task';

@Component({
    selector: 'app-documents-view',
    standalone: true,
    imports: [Button, CommonModule, Dialog, FileUpload, ToastModule, ProgressBar],
    templateUrl: './documents-view.component.html',
    styleUrl: './documents-view.component.scss'
})
export class DocumentsViewComponent implements OnInit {
    @Input() documents!: Array<ProjectDocument>;
    @Input() task_id: number = 0;
    @Input() module_id: number = 0;
    @Input() project_id: number = 0;
    uploadedFiles: any[] = [];
    requests: Array<DocumentRequest> = [];
    loading: boolean = false;
    target!: number;
    targetId!: number;

    constructor(private _documentService: ProjectDocumentControllerService) {}

    ngOnInit() {

    }

    createDocument(
    ) {
        if(this.task_id != 0) {
            this._documentService.addDocumentsToTaskManual(
                this.task_id,
                this.requests,
                this.uploadedFiles
            ).subscribe({
                next: (response) => this.updateResponse(response),
                error: (err) => console.error(err)
            });
        } else if(this.project_id != 0) {
            this._documentService.addDocumentsToProjectManual(
                this.project_id,
                this.requests,
                this.uploadedFiles
            ).subscribe({
                next: (response) => {
                    this.updateResponse(response);
                },
                error: (err) => {
                    // treat errors
                }
            });
        } else if(this.module_id != 0) {
            this._documentService.addDocumentsToModuleManual(
                this.module_id,
                this.requests,
                this.uploadedFiles
            ).subscribe({
                next: (response) => {
                    this.updateResponse(response);
                },
                error: (err) => {
                    // treat errors
                }
            });
        }
    }

    updateResponse(response: ProjectDocument[]) {
        this.documents.push(...response);
        this.requests = [];
        this.uploadedFiles = [];
    }

    onUpload(event: any) {
        this.loading = true;

        for (let file of event.files) {
            this.requests.push({
                document_name: file.name,
                doc_type: this.setDocumentType(file.type)
            });
            this.uploadedFiles.push(file);
        }
        this.createDocument();

        this.loading = false;
    }


    setDocumentType(type: any): 'IMAGE' | 'OTHER' {
        const imageTypes = ['image/jpeg', 'image/png', 'image/gif','image/jpg', 'image/webp', 'image/svg+xml'];
        return imageTypes.includes(type) ? 'IMAGE' : 'OTHER';
    }

    deleteDocument(documentId: number): void {
        if(this.task_id != 0) {
            this.targetId = this.task_id;
            this.target = 3;
        } else if(this.project_id != 0) {
            this.targetId = this.project_id;
            this.target = 1;
        } else if(this.module_id != 0) {
            this.targetId = this.module_id;
            this.target = 2;
        }
        this._documentService.deleteDocument({
            document_id: documentId,
            target: this.target!,
            target_id: this.targetId!
        }).subscribe({
            next: (response) => {
                const index = this.documents.findIndex(doc => doc.document_id === documentId);
                if (index !== -1) {
                    this.documents.splice(index, 1);
                }
            },
            error: (err) => {
                // treat errors
            }
        });
    }


    getDocumentType(document_type: 'IMAGE' | 'OTHER' | undefined) {
        switch (document_type) {
            case 'IMAGE':
                return 'Image';
            case 'OTHER':
                return 'File';
            default:
                return 'OTHER';
        }
    }
}
