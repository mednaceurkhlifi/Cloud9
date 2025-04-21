import { Component, Input, OnInit } from '@angular/core';
import { Comment, CommentControllerService, Post, PostControllerService } from '../../../api';
import { CommentComponent } from '../comment/comment.component';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CurrUserServiceService } from '../../../user/service/curr-user-service.service';

@Component({
  selector: 'app-comment-list',
  imports: [CommentComponent,ProgressSpinnerModule,CommonModule,FormsModule,ButtonModule],
  templateUrl: './comment-list.component.html',
  styleUrl: './comment-list.component.scss'
})
export class CommentListComponent implements OnInit{

    comments!: Comment[];
    empty : boolean = false;
    loading: boolean = true;
    @Input() postId!: number;
    newCommentText: string = '';
    constructor(private commentController : CommentControllerService,private postController : PostControllerService,private userService : CurrUserServiceService){
    }
    ngOnInit(): void {
        this.commentController.getCommentsPerPost(this.postId).subscribe(data =>{
            this.comments=data;
            if(data.length==0){
                this.empty=true;
            }
            this.loading=false;
        })
    }
    createComment() {
        if (!this.newCommentText.trim()) return;
        let post : Post;
        this.postController.getPost(this.postId).subscribe(data=>{
            post=data;
            const newComment: Comment = {
                content: this.newCommentText,
                post:post,
                user:this.userService.getCurrUser()
            };
            this.commentController.createComment(newComment).subscribe({
                next:(createdComment)=>{
                    this.comments.push(createdComment);
                    this.newCommentText = '';
                    this.empty = false;
                },
                error:(err)=> {
                    this.newCommentText="Failed to create comment due to toxicity"
                },

            })
        });
    }
    onCommentDeleted(deletedId:number){
        this.comments= this.comments.filter(e=> e.id!=deletedId);
    }
}
