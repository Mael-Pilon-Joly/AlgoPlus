import { Component, OnInit } from '@angular/core';
import { SafeUrl } from '@angular/platform-browser';
import { CommentModel } from 'src/app/models/comment.model';
import { CommentService } from 'src/app/services/comment.service';
import { FileservicesService } from 'src/app/services/fileservices.service';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/services/apiservices.service';
import { User } from 'src/app/models/user.model';
import { TranslateService } from '@ngx-translate/core';


@Component({
  selector: 'app-readarticle',
  templateUrl: './readarticle.component.html',
  styleUrls: ['./readarticle.component.css']
})
export class ReadarticleComponent implements OnInit {
  
  constructor(private router:Router, private fileservices: FileservicesService, private commentService: CommentService, private services:ApiService, private translateService: TranslateService ) { }
  article = history.state.data;
  image: SafeUrl | undefined;
  comments$: CommentModel[] = [];
  commentcontent= "";
  errorCommentLength = false;
  user: User ={}

  submit() {
    this.errorCommentLength = false;
    if (this.commentcontent.length < 10) {
      this.errorCommentLength = true;
    } else {
    console.log(this.article)
    this.commentService.postComment(this.article.id, this.commentcontent).subscribe((comment) => {
    console.log(comment)
    window.location.reload()
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

  deleteComment(id: number) {
    if(confirm(this.translateService.instant('deletecommentconfirmation'))) {
      this.commentService.deleteComment(id).subscribe((res) => {
        console.log(res)
        window.location.reload()
        },
        error => {
          console.log(error)
        },)
      }
  }

  ngOnInit(): void {
    this.user = this.services.getUserValue()!;

    if (this.article == undefined && localStorage.getItem("article")!= null) {
      this.article = JSON.parse(localStorage.getItem("article")!)
    }
    localStorage.setItem("article", JSON.stringify(this.article))
    if(this.article.image) {
    this.image = this.fileservices.convertBlobToImage(this.article.image.data);
    }
    this.commentService.getAllCommentsForArticle(this.article.id).subscribe((list) => {
      this.comments$ = list as CommentModel[];
      console.log(this.comments$)
      console.log(this.user)
  })
}
}

