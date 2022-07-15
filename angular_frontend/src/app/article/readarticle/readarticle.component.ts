import { Component, OnInit } from '@angular/core';
import { SafeUrl } from '@angular/platform-browser';
import { CommentModel } from 'src/app/models/comment.model';
import { CommentService } from 'src/app/services/comment.service';
import { FileservicesService } from 'src/app/services/fileservices.service';

@Component({
  selector: 'app-readarticle',
  templateUrl: './readarticle.component.html',
  styleUrls: ['./readarticle.component.css']
})
export class ReadarticleComponent implements OnInit {
  
  constructor(private fileservices: FileservicesService, private commentService: CommentService ) { }
  article = history.state.data;
  image: SafeUrl | undefined;
  comments$: CommentModel[] = [];

  ngOnInit(): void {
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

