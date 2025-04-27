import { Component, OnInit } from '@angular/core';
import { CardModule } from 'primeng/card';
import { CommonModule } from '@angular/common';
import { Button } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { ProjectFormComponent } from '../../project-form/project-form.component';
import { MeetingControllerService } from '../../../services/services/meeting-controller.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { DatePicker } from 'primeng/datepicker';
import { InputNumber } from 'primeng/inputnumber';
import { InputText } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import { Tag } from 'primeng/tag';
import { Textarea } from 'primeng/textarea';
import { Fluid } from 'primeng/fluid';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { Chip } from 'primeng/chip';
import { Meeting } from '../../../services/models/meeting';
import { Toast } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { MeetingResponse } from '../../../services/models/meeting-response';
import { Paginator } from 'primeng/paginator';
import { ProgressBar } from 'primeng/progressbar';
import { Router } from '@angular/router';
import { validateDateRange } from '../../util/validators/date-range-validator';
import { TokenService } from '../../../token-service/token.service';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions, EventClickArg  } from '@fullcalendar/core'; // useful for typechecking
import dayGridPlugin from '@fullcalendar/daygrid';

@Component({
    selector: 'app-meet-plan',
    standalone: true,
    imports: [CardModule, CommonModule, Toast, FullCalendarModule,
        ProgressBar, ConfirmDialog, FormsModule, Button, Dialog, ProjectFormComponent, DatePicker, InputNumber, InputText, Message, ReactiveFormsModule, Tag, Textarea, Fluid, Chip, Paginator],
    templateUrl: './meet-plan.component.html',
    styleUrl: './meet-plan.component.scss',
    providers: [MessageService, ConfirmationService]
})
export class MeetPlanComponent implements OnInit {
    isCreatingNew: boolean = false;
    size_my: number = 200;
    page_no_my: number = 0;
    size_other: number = 200;
    page_no_other: number = 0;
    members: string[] = [];
    meetForm!: FormGroup;
    meet: Meeting = {
        title: '',
        description: '',
        beginDate: '',
        endDate: '',
        admin: { email: '' },
        members: []
    };
    user_email!: string;
    invitedMeeting: MeetingResponse = {totalElements: 0};
    userMeeting: MeetingResponse = {totalElements: 0};
    loading: boolean = false;
    // calendar
    calendarOptions: CalendarOptions = {
        initialView: 'dayGridMonth',
        plugins: [dayGridPlugin],
        events: [],
        eventClick: this.handleUserMeetingClick.bind(this),
        eventContent: (arg) => {
            const meeting = arg.event.extendedProps;

            return {
                html: `
        <div style="
          background: white;
          border: 1px solid #dcdcdc;
          border-radius: 8px;
          padding: 8px;
          box-shadow: 0 2px 8px rgba(0,0,0,0.1);
          font-family: 'Roboto', sans-serif;
          font-size: 12px;
          display: flex;
          flex-direction: column;
          gap: 6px;
          cursor: pointer;
          overflow: hidden;
        ">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <strong style="font-size: 14px; color: #333;">${arg.event.title}</strong>
            <button style="
              background-color: #8ddd57;
              border: none;
              border-radius: 5px;
              padding: 4px 8px;
              font-size: 10px;
              font-weight: bold;
              color: white;
              cursor: pointer;
            ">Join</button>
          </div>
          <div style="font-size: 11px; color: #666;">
            <strong>Organized by: You</strong>
          </div>
          <div style="display: flex; justify-content: space-between; align-items: center; font-size: 10px; color: #999;">
            <span><i class="pi pi-calendar"></i> ${arg.timeText}</span>
          </div>
        </div>
      `
            };
        }
    };


    calendarOptionsInvited: CalendarOptions = {
        initialView: 'dayGridMonth',
        events: [],
        eventClick: this.handleInvitedMeetingClick.bind(this),
        plugins: [dayGridPlugin],
        eventContent: (arg) => {
            const meeting = arg.event.extendedProps;

            return {
                html: `
        <div style="
          background: white;
          border: 1px solid #dcdcdc;
          border-radius: 8px;
          padding: 8px;
          box-shadow: 0 2px 8px rgba(0,0,0,0.1);
          font-family: 'Roboto', sans-serif;
          font-size: 12px;
          display: flex;
          flex-direction: column;
          gap: 6px;
          cursor: pointer;
          overflow: hidden;
        ">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <strong style="font-size: 14px; color: #333;">${arg.event.title}</strong>
            <button style="
              background-color: #8ddd57;
              border: none;
              border-radius: 5px;
              padding: 4px 8px;
              font-size: 10px;
              font-weight: bold;
              color: white;
              cursor: pointer;
            ">Join</button>
          </div>
          <div style="font-size: 11px; color: #666;">
            <strong>Organized by: You</strong>
          </div>
          <div style="display: flex; justify-content: space-between; align-items: center; font-size: 10px; color: #999;">
            <span><i class="pi pi-calendar"></i> ${arg.timeText}</span>
          </div>
        </div>
      `
            };
        }
    };


    constructor(
        private _meetingService: MeetingControllerService,
        private _tokenService: TokenService,
        private _messageService: MessageService,
        private confirmationService: ConfirmationService,
        private router: Router
    ) {}

    ngOnInit() {
        this.user_email = this._tokenService.getUserEmail()!;
        this.getInvitedMeeting();
        this.getUserMeeting();
        this.meetForm = new FormGroup({
            title: new FormControl('', [Validators.required]),
            description: new FormControl('', [Validators.required]),
            beginDateDate: new FormControl(null, [Validators.required]),
            beginDateTime: new FormControl(null, [Validators.required]),
            deadlineTime: new FormControl(null, [Validators.required]),
            members: new FormControl('', [Validators.required])
        }, {
            validators: validateDateRange('beginDateDate', 'beginDateTime', 'beginDateDate', 'deadlineTime')
        });
    }

    initForm() {
        this.meetForm = new FormGroup({
            title: new FormControl('', [Validators.required]),
            description: new FormControl('', [Validators.required]),
            beginDateDate: new FormControl(null, [Validators.required]),
            beginDateTime: new FormControl(null, [Validators.required]),
            deadlineTime: new FormControl(null, [Validators.required]),
            members: new FormControl('', [Validators.required])
        }, {
            validators: validateDateRange('beginDateDate', 'beginDateTime', 'beginDateDate', 'deadlineTime')
        });
    }

    getInvitedMeeting() {
        this._meetingService
            .getInvitedMeetings({
                user_email: this.user_email,
                size: this.size_other,
                page_no: this.page_no_other
            })
            .subscribe({
                next: (response) => {
                    this.invitedMeeting = response;
                    this.loadInvitedMeetings();
                },
                error: (err) => {}
            });
    }

    getUserMeeting(): void {
        this._meetingService
            .getUserMeetings({
                user_email: this.user_email,
                size: this.size_my,
                page_no: this.page_no_my
            })
            .subscribe({
                next: (response) => {
                    this.userMeeting = response;
                    this.loadMeetings();
                },
                error: (err) => {
                    console.error('Error fetching meetings', err);
                }
            });
    }

    loadMeetings(): void {
        this.calendarOptions = {
            ...this.calendarOptions,
            events: this.userMeeting?.meetings?.map(m => ({
                id: m.meetingId?.toString(),
                title: m.title,
                start: m.beginDate,
                end: m.endDate,
                description: m.description,
            }))
        };
    }

    loadInvitedMeetings(): void {
        this.calendarOptionsInvited = {
            ...this.calendarOptionsInvited,
            events: this.invitedMeeting?.meetings?.map(m => ({
                id: m.meetingId?.toString(),
                title: m.title,
                start: m.beginDate,
                end: m.endDate,
                description: m.description,
            }))
        };
    }


    createMeeting() {
        if (this.setRequest()) {
            this.loading = true;

            this._meetingService
                .createNewMeeting({
                    body: this.meet
                })
                .subscribe({
                    next: (response) => {
                        this.getUserMeeting();
                        this.cancel();
                        this.loading = false;
                    },
                    error: (err) => {
                        console.error('Meeting creation failed', err);
                        this.loading = false;
                    }
                });
        }
    }


    cancel() {
        this.isCreatingNew = false;
        this.members = [];
        this.setMeetMembers();
        this.initForm();
    }

    showForm() {
        this.isCreatingNew = true;
        this.initForm();
    }

    setMeetMembers() {
        let keywords: string = '';
        let keyWords = this.members;
        for (let key of keyWords) {
            keywords += key + ', ';
        }
        this.meetForm.get('members')?.setValue(keywords);
    }

    addKeyword(event: any): void {
        const input = (event.target as HTMLInputElement).value.trim();
        const emailRegex: RegExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (input) {
            if (this.members.includes(input)) {
                this._messageService.add({ severity: 'error', summary: 'Member duplicated', detail: 'Each member must be present one time', life: 3000 });
            } else if (this.user_email == input) {
                this._messageService.add({ severity: 'error', summary: 'Member duplicated', detail: 'You are the room admin !', life: 3000 });
            } else if (!emailRegex.test(input)) {
                this._messageService.add({ severity: 'error', summary: 'Incorrect format', detail: 'You need to enter a valid email.', life: 3000 });
            } else {
                this.members.push(input);
                (event.target as HTMLInputElement).value = '';
            }
        }
        event.preventDefault();
        this.setMeetMembers();
    }

    removeKeyword(index: number): void {
        this.members.splice(index, 1);
        this.setMeetMembers();
    }

    setRequest(): boolean {
        this.meet.title = this.meetForm.get('title')?.value;
        this.meet.description = this.meetForm.get('description')?.value;
        if (this.beginDateTime) this.meet.beginDate = this.beginDateTime;
        if (this.deadlineDateTime) this.meet.endDate = this.deadlineDateTime;
        this.meet.admin = { email: this.user_email };
        if (this.members.length > 0) {
            for (let m of this.members) {
                this.meet.members?.push({ email: m });
            }
            return true;
        }
        return false;
    }

    joinUserMeet(meetingId: number | undefined) {
        let toJoin = this.userMeeting.meetings?.find(m => m.meetingId == meetingId);
        let user = toJoin!.admin!;
        const url = `/meeting/${meetingId}/${user.userId}/${user.fullName}`;
        window.open(url, '_blank');
    }

    handleUserMeetingClick(info: EventClickArg): void {
        const meetingId = Number(info.event.id);
        this.joinUserMeet(meetingId);
    }


    joinInvitedMeet(meetingId: number | undefined) {
        let toJoin = this.invitedMeeting.meetings?.find(m => m.meetingId == meetingId);
        let user = toJoin!.members!.find(m => m.email == this.user_email)!;
        const url = `/meeting/${meetingId}/${user.userId}/${user.fullName}`;
        window.open(url, '_blank');
    }

    handleInvitedMeetingClick(info: EventClickArg): void {
        const meetingId = Number(info.event.id);
        this.joinInvitedMeet(meetingId);
    }


    get beginDateTime() {
        const date = this.meetForm.get('beginDateDate')?.value;
        const time = this.meetForm.get('beginDateTime')?.value;

        if (!date || !time) return null;

        return new Date(date.getFullYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds()).toISOString();
    }

    get deadlineDateTime() {
        const date = this.meetForm.get('beginDateDate')?.value;
        const time = this.meetForm.get('deadlineTime')?.value;

        if (!date || !time) return null;

        return new Date(date.getFullYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds()).toISOString();
    }

/*
    onPageChangeUserMeeting($event: any) {
        this.page_no_my = $event.page;
        this.size_my = $event.rows;
        this.getUserMeeting();
    }

    onPageChangeInvMeeting($event: any) {
        this.page_no_other = $event.page;
        this.size_other = $event.rows;
        this.getInvitedMeeting();
    }

    deleteMeeting(meetingId: number | undefined) {
        this.confirmationService.confirm({
            message: 'Do you want to delete this meet?',
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
                this._meetingService
                    .deleteMeeting({
                        meeting_id: meetingId!
                    })
                    .subscribe({
                        next: () => {
                            this.getUserMeeting();
                        },
                        error: (err) => {
                            // treat errors
                        }
                    });
            },
            reject: () => {
                this._messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected' });
            }
        });
    }*/
}
