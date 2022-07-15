import { Component, OnInit } from '@angular/core';
import { SafeUrl } from '@angular/platform-browser';
import { CommentModel } from 'src/app/models/comment.model';
import { CommentService } from 'src/app/services/comment.service';
import { FileservicesService } from 'src/app/services/fileservices.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-readarticle',
  templateUrl: './readarticle.component.html',
  styleUrls: ['./readarticle.component.css']
})
export class ReadarticleComponent implements OnInit {
  
  constructor(private router:Router, private fileservices: FileservicesService, private commentService: CommentService ) { }
  article = history.state.data;
  image: SafeUrl | undefined;
  comments$: CommentModel[] = [];
  commentcontent= "";
  errorCommentLength = false;
  submit() {
    this.errorCommentLength = false;
    if (this.commentcontent.length < 10) {
      this.errorCommentLength = true;
    } else {
    console.log(this.article)
    this.commentService.postComment(this.article.id, this.commentcontent).subscribe((comment) => {
    console.log(comment)
    },
    error => {
      console.log(error)
      this.router.navigate(['/login']);
    },)
  }
}

  convertDataToImage(c: any) {
    console.log(c);
    if (c.user_avatar != null) {
    return  this.fileservices.convertBlobToImage(c.user_avatar.data)
    } else {
     return" ../../../assets/avatar.png"
    }
  }
  ngOnInit(): void {
    if (this.article == undefined && localStorage.getItem("article")!= null) {
      this.article = JSON.parse(localStorage.getItem("article")!)
    }
    localStorage.setItem("article", JSON.stringify(this.article))
    console.log(this.article)
    if(this.article.image) {
    this.image = this.fileservices.convertBlobToImage(this.article.image.data);
    }
    this.commentService.getAllCommentsForArticle(this.article.id).subscribe((list) => {
      this.comments$ = list as CommentModel[];
      console.log(this.comments$)
  })
}
}

