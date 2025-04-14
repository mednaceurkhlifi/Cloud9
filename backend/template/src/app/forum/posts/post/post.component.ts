import { Component, Input, OnInit } from '@angular/core';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { PanelModule } from 'primeng/panel';
import { Post, PostControllerService, Vote, VoteControllerService } from '../../../api';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post',
  imports: [CommonModule,PanelModule, AvatarModule, ButtonModule, MenuModule],
  standalone:true,
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss'
})
export class PostComponent implements OnInit{
    items: { label?: string; icon?: string; separator?: boolean }[] = [];
    post: Post;
    votes : Vote[];
    voteNum: number;
    @Input() postId?: number;
    finalPostId!:number;
    constructor(private postController : PostControllerService,private route : ActivatedRoute,private voteController: VoteControllerService){
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
                label: 'Refresh',
                icon: 'pi pi-refresh'
            },
            {
                label: 'Search',
                icon: 'pi pi-search'
            },
            {
                separator: true
            },
            {
                label: 'Delete',
                icon: 'pi pi-times'
            }
        ];
    }
}
