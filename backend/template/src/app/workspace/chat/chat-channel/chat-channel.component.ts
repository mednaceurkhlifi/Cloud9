import { Component, Input, OnInit } from '@angular/core';
import { InputText } from 'primeng/inputtext';
import { FloatLabel } from 'primeng/floatlabel';
import { Textarea } from 'primeng/textarea';
import { Button } from 'primeng/button';
import { FileUpload } from 'primeng/fileupload';
import { CommonModule, NgForOf, NgIf } from '@angular/common';
import { DocumentRequest } from '../../../services/models/document-request';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { WorkspaceMessageControllerService } from '../../../services/services/workspace-message-controller.service';
import { TokenService } from '../util/token.service';
import { Message } from 'primeng/message';
import { WorkspaceMessage } from '../../../services/models/workspace-message';
import { MessageResponse } from '../../../services/models/message-response';
import { ChatService } from '../../../socket/ChatService';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { Toast } from 'primeng/toast';
import { Avatar } from 'primeng/avatar';
import { MessageService } from 'primeng/api';
import { MessageDto } from '../../../services/models/message-dto';

@Component({
    selector: 'app-chat-channel',
    standalone: true,
    imports: [InputText, FloatLabel, Textarea, Button, FileUpload, NgForOf, NgIf, FormsModule, Message, ReactiveFormsModule, CommonModule, Toast, Avatar],
    templateUrl: './chat-channel.component.html',
    styleUrl: './chat-channel.component.scss',
    providers: [MessageService]
})
export class ChatChannelComponent implements OnInit {
    msgForm!: FormGroup;
    uploadedFiles: any[] = [];
    attachements_request: Array<DocumentRequest> = [];
    loading: boolean = false;
    user_email!: string | null;
    target: number = 0;
    targetName: string = 'workspace';
    messageResponse!: MessageResponse;
    @Input() workspace_id: number = 0;
    @Input() project_id: number = 0;
    @Input() task_id: number = 0;
    @Input() module_id: number = 0;
    targetId: number = 0;
    size: number = 100;
    page_no: number = 0;
    socketClient!: any;
    visible: boolean = false;
    comingMessage: MessageDto = {};

    constructor(
        private _messageService: WorkspaceMessageControllerService,
        private _tokenService: TokenService,
        private _chatService: ChatService,
        private messageService: MessageService
    ) {}

    ngOnInit() {
        this.user_email = this._tokenService.email;

        this.msgForm = new FormGroup({
            message: new FormControl('', [Validators.required])
        });
        if (this.workspace_id > 0) {
            this.target = 1;
            this.targetId = this.workspace_id;
            this._messageService
                .getWorkspaceMessages({
                    workspaceId: this.targetId,
                    size: this.size,
                    page_no: this.page_no
                })
                .subscribe({
                    next: (response) => {
                        this.messageResponse = response;
                    },
                    error: (err) => {}
                });
        } else if (this.project_id > 0) {
            this.target = 2;
            this.targetName = 'project';
            this.targetId = this.project_id;
            this._messageService
                .getProjectMessages({
                    projectId: this.targetId,
                    size: this.size,
                    page_no: this.page_no
                })
                .subscribe({
                    next: (response) => {
                        this.messageResponse = response;
                    },
                    error: (err) => {}
                });
        } else if (this.module_id > 0) {
            this.target = 3;
            this.targetName = 'module';
            this.targetId = this.module_id;
            this._messageService
                .getModuleMessages({
                    moduleId: this.targetId,
                    size: this.size,
                    page_no: this.page_no
                })
                .subscribe({
                    next: (response) => {
                        this.messageResponse = response;
                    },
                    error: (err) => {}
                });
        } else if (this.task_id > 0) {
            this.target = 4;
            this.targetName = 'task';
            this.targetId = this.task_id;
            this._messageService
                .getTaskMessages({
                    taskId: this.targetId,
                    size: this.size,
                    page_no: this.page_no
                })
                .subscribe({
                    next: (response) => {
                        this.messageResponse = response;
                    },
                    error: (err) => {}
                });
        }
        // this.socketClient = this._chatService.connect();
        const socket = new SockJS('http://localhost:8082/api/v1/ws');
        this.socketClient = Stomp.over(socket);

        this.socketClient.connect({}, () => {
            console.log('Connected to WebSocket');
            const topic = `/user/${this.targetId}/${this.getTargetName()}`;
            console.log(topic);
            this.socketClient.subscribe(topic, (message: any) => {
                this.comingMessage = JSON.parse(message.body);
                if(this.comingMessage.sender?.email != this.user_email) {
                    if (!this.visible) {
                        this.messageService.add({ key: 'confirm', sticky: true, severity: 'success', summary: this.comingMessage.message });
                        this.visible = true;
                        this.messageResponse.messages?.push(this.comingMessage);
                    }
                }
            });
        });
    }

    setDocumentType(type: any): 'IMAGE' | 'OTHER' {
        const imageTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/jpg', 'image/webp', 'image/svg+xml'];
        return imageTypes.includes(type) ? 'IMAGE' : 'OTHER';
    }

    private getTargetName(): 'workspace' | 'project' | 'module' | 'task' | 'unknown' {
        switch (this.target) {
            case 1:
                return 'workspace';
            case 2:
                return 'project';
            case 3:
                return 'module';
            case 4:
                return 'task';
        }
        return 'unknown';
    }

    sendMessage() {
        let message: WorkspaceMessage = {
            message: this.msgForm.get('message')?.value
        };
        if (this.uploadedFiles.length > 0) {
            for (let file of this.uploadedFiles) {
                this.attachements_request.push({
                    document_name: file.name,
                    doc_type: this.setDocumentType(file.type)
                });
                this.uploadedFiles.push(file);
            }
            this._messageService.sendMessageToTarget(this.target, this.targetId, this.user_email!, message, this.attachements_request, this.uploadedFiles).subscribe({
                next: (response) => {
                    this.msgForm.reset();
                    this.messageResponse.messages?.push(response);
                },
                error: (error) => {
                    // treat errors
                }
            });
        } else {
            this._messageService.sendMessageToTarget(this.target, this.targetId, this.user_email!, message, this.attachements_request, this.uploadedFiles).subscribe({
                next: (response) => {
                    this.msgForm.reset();
                    this.messageResponse.messages?.push(response);
                },
                error: (error) => {
                    // treat errors
                }
            });
        }
    }

    onConfirm() {

    }

    onReject() {
        this.visible = false;
    }
}
