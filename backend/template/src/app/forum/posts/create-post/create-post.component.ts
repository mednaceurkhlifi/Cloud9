import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { FloatLabel, FloatLabelModule } from 'primeng/floatlabel';
import { InputText, InputTextModule } from 'primeng/inputtext';
import { Textarea, TextareaModule } from 'primeng/textarea';
import { Post, PostControllerService } from '../../../api';
import { CurrUserServiceService } from '../../../user/service/curr-user-service.service';
import { FileUploadModule, UploadEvent } from 'primeng/fileupload';

@Component({
  selector: 'app-create-post',
  imports: [CommonModule,TextareaModule,InputTextModule,DialogModule,ButtonModule,ReactiveFormsModule,FloatLabel,FileUploadModule],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.scss'
})
export class CreatePostComponent implements OnInit{
    postForm!: FormGroup;
    visible: boolean = true;
    header : string ="Create post"
    id! :string |null;
    post! :Post ;
    selectedFile! : File;
    constructor(private formBuilder:FormBuilder,private router: Router,private postController : PostControllerService, private userService : CurrUserServiceService,private route : ActivatedRoute){}
    ngOnInit(): void {
        this.id = this.route.snapshot.paramMap.get("id");
        this.postForm=this.formBuilder.group({
            title:['',Validators.required],
            content:['',[Validators.required,Validators.minLength(10)]],
        })
        if(this.id!=null){
            this.postController.getPost(parseInt(this.id)).subscribe(data => {
                this.post=data;
                this.postForm.patchValue({
                    title: this.post.title,
                    content: this.post.content
                });
            });

        }
    }
    onClose(){
        this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
            this.router.navigate(['/posts']);
        });}
    onSave(){
        if(this.postForm.valid){
            const title = this.postForm.get("title")?.value;
            const content = this.postForm.get("content")?.value;
            this.post = {
                title:title,
                content:content,
                user:this.userService.getCurrUser(),
                image:undefined,
                date:undefined
            };
            if(this.id==null){
                this.postController.createPost(this.post,this.selectedFile!).subscribe({
                    next: (res)=>{
                        console.log("post created",res);
                        this.onClose();
                    },
                    error:(err)=>{
                        console.error(err);
                    }

                })
            }
            else{
                this.post.id=parseInt(this.id);

                this.postController.updatePost(this.post).subscribe({
                    next: (res)=>{
                        console.log("post created",res);
                        this.onClose();
                    },
                    error:(err)=>{
                        console.error(err);
                    }

                })
            }
        }
    }
    onHide(){
        this.onClose();
    }
    onFileSelect(event :any){
        const file = event.files?.[0];
        if(file){
            console.log(file);
            this.selectedFile=file;
        }
    }
}
