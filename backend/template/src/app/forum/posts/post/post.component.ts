import { Component, Input, OnInit } from '@angular/core';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { PanelModule } from 'primeng/panel';
import { Post, PostControllerService, PostDTO, Vote, VoteControllerService, VoteDTO } from '../../../api';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { CurrUserServiceService } from '../../../user/service/curr-user-service.service';
import { CommentListComponent } from '../../comments/comment-list/comment-list.component';
import { animate, style, transition, trigger } from '@angular/animations';
import { MarkdownModule, MarkdownService, provideMarkdown } from 'ngx-markdown';

@Component({
  selector: 'app-post',
  imports: [CommonModule,PanelModule, AvatarModule, ButtonModule, MenuModule,ToastModule,ConfirmDialog,CommentListComponent,RouterOutlet,MarkdownModule],
  standalone:true,
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss',
  providers:[ConfirmationService,MessageService,provideMarkdown()],
  animations: [
    trigger('fadeSlide', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(10px)' }),
        animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' })),
      ]),
      transition(':leave', [
        animate('200ms ease-in', style({ opacity: 0, transform: 'translateY(10px)' })),
      ]),
    ]),
  ],

})
export class PostComponent implements OnInit{
    items: MenuItem[] = [];
    post: PostDTO;
    votes : VoteDTO[];
    voteNum: number;
    @Input() postId?: number;
    finalPostId!:number;
    owner: boolean= false;
    vote! :Vote
    routePost : boolean = false;
    showComments: boolean = false;
    constructor(private postController : PostControllerService,
                private route : ActivatedRoute,
                private voteController: VoteControllerService,
                private userService: CurrUserServiceService,
                private router : Router,
                private confirmationService: ConfirmationService,
                private messageService: MessageService,
                private markDownService :MarkdownService){
        this.votes=[];
        this.post= {};
        this.voteNum=0;
    }
    ngOnInit() {
         let routeId=this.route.snapshot.paramMap.get("id");
        if(routeId!=null){
            this.routePost=true;
            this.finalPostId=parseInt(routeId);
        }else{
            if(this.postId!=undefined)
                this.finalPostId=this.postId
        }
        if(this.finalPostId!=null){
            this.postController.getPost(this.finalPostId).subscribe(data =>{
                this.post=data;
                if(this.post.userId==this.userService.getCurrUser().user_id){
                    this.owner=true;
                }
            });
            this.voteController.getVotesPerPost(this.finalPostId).subscribe(data => {
                this.votes=data;
                this.votes.forEach(v => {
                    if(v.voteType == "UPVOTE")
                        this.voteNum++;
                    else
                        this.voteNum--;
                })
            });

        }

        this.items = [
            {
                label: 'Edit',
                icon: 'pi pi-pencil',
                command:()=>{
                    this.router.navigate(["/posts",{ outlets: { create: ['post', this.post.id] } }]);
                }

            },
            {
                separator: true
            },
            {
                label: 'Delete',
                icon: 'pi pi-trash',
                command:(event)=> this.confirm2(event)
            }
        ];
    }
    confirm2(event: any) {
        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: 'Do you want to delete this post?',
            header: 'Danger Zone',
            icon: 'pi pi-info-circle',
            rejectLabel: 'Cancel',
            rejectButtonProps: {
                label: 'Cancel',
                severity: 'secondary',
                outlined: true,
            },
            acceptButtonProps: {
                label: 'Delete',
                severity: 'danger',
            },

            accept: () => {
                if(this.post.id!=undefined){
                    this.postController.deletePost(this.post.id).subscribe({
                        next: () => {
                            this.messageService.add({ severity: 'info', summary: 'Confirmed', detail: 'Post deleted' });

                            this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
                                this.router.navigate(['/posts']);
                            });
                        },
                        error: err => {
                            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete post' });
                            console.error('Delete error:', err);
                        }
                    });}
            },
            reject: () => {
                this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected' });
            },
        });
    }
    upVote() {
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
            votable: this.post, // or this.comment
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
            votable: this.post, // or this.comment
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
    fade(){
        this.router.navigate(['/post', this.post.id]);
    }
}
