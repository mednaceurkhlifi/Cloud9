import { Component, OnInit } from '@angular/core';
import { Post, PostControllerService } from '../../../api';
import { CommonModule } from '@angular/common';
import { PostComponent } from '../post/post.component';
import { RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-post-list',
  imports: [RouterOutlet,RouterModule,CommonModule,PostComponent],
  templateUrl: './post-list.component.html',
  styleUrl: './post-list.component.scss',
  standalone:true
})
export class PostListComponent implements OnInit{
    posts: Post[]
    constructor(private postController : PostControllerService){
        this.posts=[];
    }
    ngOnInit(): void {
        this.postController.getPosts().subscribe(data =>{
            this.posts=data;
            console.log(data);
        })
    }

}
