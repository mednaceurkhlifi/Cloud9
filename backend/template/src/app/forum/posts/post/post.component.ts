import { Component, Input, OnInit } from '@angular/core';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { PanelModule } from 'primeng/panel';
import { Post, PostControllerService, Vote, VoteControllerService } from '../../../api';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CurrUserServiceService } from '../../../user/service/curr-user-service.service';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialog } from 'primeng/confirmdialog';

@Component({
  selector: 'app-post',
  imports: [CommonModule,PanelModule, AvatarModule, ButtonModule, MenuModule,ToastModule,ConfirmDialog],
  standalone:true,
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss',
  providers:[ConfirmationService,MessageService]

})
export class PostComponent implements OnInit{
    items: MenuItem[] = [];
    post: Post;
    votes : Vote[];
    voteNum: number;
    @Input() postId?: number;
    finalPostId!:number;
    owner: boolean= false;
    constructor(private postController : PostControllerService,private route : ActivatedRoute,private voteController: VoteControllerService,private userService: CurrUserServiceService,private router : Router,private confirmationService: ConfirmationService, private messageService: MessageService){
        this.votes=[];
        this.post= {};
        this.voteNum=0;
    }
    ngOnInit() {
        let routeId=this.route.snapshot.paramMap.get("id");
        if(routeId!=null){
            this.finalPostId=parseInt(routeId);
        }else{
            if(this.postId!=undefined)
                this.finalPostId=this.postId
        }
        if(this.finalPostId!=null){
            this.postController.getPost(this.finalPostId).subscribe(data =>{
                this.post=data;
                if(this.post.user?.user_id==this.userService.getCurrUser().user_id){
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
                icon: 'pi pi-search',
                command:()=>{
                    this.router.navigate([{ outlets: { create: ['post', this.post.id] } }], {
                        relativeTo: this.route
                    });
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
}
