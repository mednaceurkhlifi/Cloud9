import { Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { Comment, CommentControllerService, CommentDTO, User, Vote, VoteControllerService, VoteDTO } from '../../../api';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { CurrUserServiceService } from '../../../user/service/curr-user-service.service';
import { AvatarModule } from 'primeng/avatar';
import { PanelModule } from 'primeng/panel';
import { MenuModule } from 'primeng/menu';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialog, ConfirmDialogModule } from 'primeng/confirmdialog';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-comment',
  imports: [ButtonModule,CommonModule,AvatarModule,PanelModule,MenuModule,ToastModule,ConfirmDialogModule,FormsModule],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.scss',
  providers:[ConfirmationService,MessageService]
})
export class CommentComponent  implements OnInit{
    comment! : CommentDTO
    votes!: VoteDTO[];
    @Input() commentId?: number;
    finalCommentId!:number
    voteNum:number
    vote!: Vote
    owner : boolean = false;
    items : MenuItem[]=[];
    author! : User
    @Output() deleted= new EventEmitter<number>();
    editMode=false;
    editableText="";
    constructor(private commentController: CommentControllerService,private route: ActivatedRoute,private userService : CurrUserServiceService,private voteController:VoteControllerService,private confirmationService : ConfirmationService,private messageService : MessageService){

        this.voteNum=0;
    }
    ngOnInit(): void {
    this.items = [
        {
            label: 'Edit',
            icon: 'pi pi-search',
            command:()=>{
                console.log("bro")
            }

        },
        {
            separator: true
        },
        {
            label: 'Delete',
            icon: 'pi pi-trash',
            command:()=>console.log("bro")
        }
    ];
        this.finalCommentId= this.commentId?? parseInt(this.route.snapshot.paramMap.get("id")!)

        this.commentController.getComment(this.finalCommentId).subscribe(data => {
            this.comment=data;
            if(this.comment.userId==this.userService.getCurrUser().user_id)
                this.owner= true;
        })
        this.voteController.getVotesPerComment(this.finalCommentId).subscribe(data => {
            this.votes=data;
            console.log(this.votes);
            this.votes.forEach(v => {
                if(v.voteType == "UPVOTE")
                    this.voteNum++;
                else
                    this.voteNum--;
            })
        })
    }
    upVote(){
        const currentUser = this.userService.getCurrUser();
        const currentUserId = String(currentUser.user_id);

        const existingVote = this.votes.find(v => String(v.userId) === currentUserId);

        if (existingVote) {
            if (existingVote.voteType === "UPVOTE") {
                return;
            }

            if (existingVote.voteType === "DOWNVOTE") {
                this.voteNum += 2;

                this.voteController.getVote(existingVote.id!).subscribe(originalVote => {
                    originalVote.voteType = "UPVOTE";

                    this.voteController.updateVote(originalVote).subscribe({
                        next: () => {
                            existingVote.voteType = "UPVOTE";
                            console.log("Vote changed to UPVOTE");
                        },
                        error: err => console.error("Error updating vote:", err)
                    });
                });

                return;
            }
        }

        const newVote: Vote = {
            voteType: "UPVOTE",
            votable: this.comment, // or this.comment
            user: currentUser
        };

        this.voteNum++;
        this.voteController.createVote(newVote).subscribe({
            next: (createdVote) => {
                console.log("New UPVOTE created:", createdVote);
                this.votes.push({ ...createdVote, userId: parseInt(currentUserId), voteType: "UPVOTE" }); // add to local list
            },
            error: err => console.error("Error creating vote:", err)
        });
    }
    downVote(){
        const currentUser = this.userService.getCurrUser();
        const currentUserId = String(currentUser.user_id);

        const existingVote = this.votes.find(v => String(v.userId) === currentUserId);

        if (existingVote) {
            if (existingVote.voteType === "DOWNVOTE") {
                return;
            }

            if (existingVote.voteType === "UPVOTE") {
                this.voteNum -= 2;

                this.voteController.getVote(existingVote.id!).subscribe(originalVote => {
                    originalVote.voteType = "DOWNVOTE";

                    this.voteController.updateVote(originalVote).subscribe({
                        next: () => {
                            existingVote.voteType = "DOWNVOTE";
                            console.log("Vote changed to DOWNVOTE");
                        },
                        error: err => console.error("Error updating vote:", err)
                    });
                });

                return;
            }
        }

        const newVote: Vote = {
            voteType: "DOWNVOTE",
            votable: this.comment, // or this.comment
            user: currentUser
        };

        this.voteNum++;
        this.voteController.createVote(newVote).subscribe({
            next: (createdVote) => {
                console.log("New DOWNVOTE created:", createdVote);
                this.votes.push({ ...createdVote, userId: parseInt(currentUserId), voteType: "DOWNVOTE" }); // add to local list
            },
            error: err => console.error("Error creating vote:", err)
        });

    }
    edit(){
        this.editMode=true;
        this.editableText =this.comment.content!;
    }
    saveEdit(){
        if (this.comment?.id && this.editableText.trim()) {
            const updated = { ...this.comment, content: this.editableText };
            this.commentController.updateComment(updated).subscribe({
                next: (updatedComment) => {
                    this.comment = updatedComment;
                    this.editMode = false;
                    this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Comment updated' });
                },
                error: (err) => {
                    console.error(err);
                    this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update comment' });
                },
            });
        }
    }
    delete(){
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete this comment?',
            header: 'Delete Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                if (this.comment?.id) {
                    this.commentController.deleteComment(this.comment.id).subscribe({
                        next: () => {
                            this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Comment deleted' });
                            this.deleted.emit(this.comment.id);
                        },
                        error: (err) => {
                            console.error(err);
                            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete comment' });
                        },
                    });
                }
            },
        });
    }
}
