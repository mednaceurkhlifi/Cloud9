import { Component, Input, OnInit } from '@angular/core';
import { CommentComponent } from '../comment/comment.component';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CurrUserServiceService } from '../../../user/service/curr-user-service.service';
import { Comment, CommentControllerService, CommentDTO, Post, PostControllerService } from '../../api';
import { CommentServiceService } from '../../services/comment-service.service';
import { DropdownModule } from 'primeng/dropdown';

@Component({
  selector: 'app-comment-list',
  imports: [CommentComponent,ProgressSpinnerModule,CommonModule,FormsModule,ButtonModule,DropdownModule],
  templateUrl: './comment-list.component.html',
  styleUrl: './comment-list.component.scss',
    standalone: true
})
export class CommentListComponent implements OnInit{

    comments!: CommentDTO[];
    empty : boolean = false;
    loading: boolean = true;
    @Input() postId!: number;
    newCommentText: string = '';
    selectedFilter: any = null;
    constructor(private commentController : CommentControllerService,private postController : PostControllerService,private userService : CurrUserServiceService,private commentService: CommentServiceService){
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
    filterOptions = [
        { label: 'All', value: null },
        { label: 'Positive 😊', value: 'positive' },
        { label: 'Neutral 😐', value: 'neutral' },
        { label: 'Negative 😠', value: 'negative' }
    ];
    sortOptions = [
        { label: 'Newest', value: 'newest' },
        { label: 'Oldest', value: 'oldest' },
        { label: 'Most Upvotes', value: 'upvotes' },
        { label: 'Most Downvotes', value: 'downvotes' }
    ];

    selectedSort = 'newest';
    searchQuery : string='';
    resetFilters() {
        this.selectedFilter = null;
    }
    get filteredComments() {

        let result= this.comments.filter(comment => {
            const matchesSearch = comment.content!.toLowerCase().includes(this.searchQuery.toLowerCase());
            const matchesFilter = !this.selectedFilter || comment.sentiment?.toLowerCase() === this.selectedFilter.value;

            return matchesSearch && matchesFilter;
        });
        switch (this.selectedSort) {
            case 'newest':
                result = result.sort((a, b) => new Date(b.date!).getTime() - new Date(a.date!).getTime());
            break;
            case 'oldest':
                result = result.sort((a, b) => new Date(a.date!).getTime() - new Date(b.date!).getTime());
            break;
            case 'upvotes':
                result = result.sort((a, b) => (this.commentService.getCommentVotes(b) ?? 0) - (this.commentService.getCommentVotes(a) ?? 0));
            break;
            case 'downvotes':
                result = result.sort((a, b) => (this.commentService.getCommentVotes(a) ?? 0) - (this.commentService.getCommentVotes(b) ?? 0));
            break;
        }
        return result;
    }
}
