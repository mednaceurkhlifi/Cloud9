import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post/post.component';
import { ActivatedRoute, Router, RouterModule, RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { ProgressSpinner } from 'primeng/progressspinner';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { FooterWidget } from '../../../pages/landing/components/footerwidget';
import { TopbarWidget } from '../../../pages/landing/components/topbarwidget.component';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { PostControllerService, PostDTO } from '../../api';
import { PostServiceService } from '../../services/post-service.service';

@Component({
  selector: 'app-post-list',
  imports: [RouterOutlet,RouterModule,CommonModule,PostComponent,ButtonModule,ProgressSpinner,ToastModule,FooterWidget,TopbarWidget,FormsModule,DropdownModule],
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.scss',
  standalone:true,
  providers:[MessageService]
})
export class PostListComponent implements OnInit{
    posts: PostDTO[];
    empty : boolean = false;
    loading: boolean = true;
    showHeaderFooter : boolean = false;
    searchQuery: string = '';
    selectedFilter: any = null;

    constructor(private postController : PostControllerService,private router : Router,private route : ActivatedRoute,private postService : PostServiceService){
        this.posts=[];
    }
    ngOnInit(): void {
        let url = this.router.url;
        this.showHeaderFooter = url.includes("postList")
        this.postController.getPosts().subscribe(data =>{
            this.posts=data;
            if(data.length==0){
                this.empty=true;
            }
            this.posts.sort((a, b) => new Date(b.date!).getTime() - new Date(a.date!).getTime());
            this.loading=false;
        })
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
    resetFilters() {
        this.searchQuery = '';
        this.selectedFilter = null;
    }
    get filteredPosts() {
        let result= this.posts.filter(post => {
            const matchesSearch = post.title!.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                post.content!.toLowerCase().includes(this.searchQuery.toLowerCase());
            const matchesFilter = !this.selectedFilter || post.sentiment?.toLowerCase() === this.selectedFilter.value;

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
                result = result.sort((a, b) => (this.postService.getPostVotes(b) ?? 0) - (this.postService.getPostVotes(a) ?? 0));
            break;
            case 'downvotes':
                result = result.sort((a, b) => (this.postService.getPostVotes(a) ?? 0) - (this.postService.getPostVotes(b) ?? 0));
            break;
        }
        return result;
    }
}
