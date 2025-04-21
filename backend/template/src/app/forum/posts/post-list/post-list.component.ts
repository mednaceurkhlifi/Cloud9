import { Component, OnInit } from '@angular/core';
import { Post, PostControllerService } from '../../../api';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post/post.component';
import { RouterModule, RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { ProgressSpinner } from 'primeng/progressspinner';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-post-list',
  imports: [RouterOutlet,RouterModule,CommonModule,PostComponent,ButtonModule,ProgressSpinner,ToastModule],
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.scss',
  standalone:true,
  providers:[MessageService]
})
export class PostListComponent implements OnInit{
    posts: Post[];
    empty : boolean = false;
    loading: boolean = true;

    constructor(private postController : PostControllerService){
        this.posts=[];
    }
    ngOnInit(): void {
        this.postController.getPosts().subscribe(data =>{
            this.posts=data;
            if(data.length==0){
                this.empty=true;
            }
            this.loading=false;
        })
    }

}
