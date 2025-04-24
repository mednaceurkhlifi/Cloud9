import { Component, OnInit } from '@angular/core';
import { Post, PostControllerService, PostDTO } from '../../../api';
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

    constructor(private postController : PostControllerService,private router : Router,private route : ActivatedRoute){
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

    resetFilters() {
        this.searchQuery = '';
        this.selectedFilter = null;
    }
    get filteredPosts() {
        return this.posts.filter(post => {
            const matchesSearch = post.title!.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                post.content!.toLowerCase().includes(this.searchQuery.toLowerCase());
            if(this.selectedFilter!=null)
                console.log(post.sentiment,this.selectedFilter.value);
            const matchesFilter = !this.selectedFilter || post.sentiment?.toLowerCase() === this.selectedFilter.value;

            return matchesSearch && matchesFilter;
        });
    }
}
