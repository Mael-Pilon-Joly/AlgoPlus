import { Component, OnInit } from '@angular/core';
import { AllCompleteArticle } from 'src/app/models/allArticlesResponse.model';
import { CompleteArticle } from 'src/app/models/completearticle.model';
import { ArticleserviceService } from 'src/app/services/articleservice.service';
import { FileservicesService } from 'src/app/services/fileservices.service';

@Component({
  selector: 'app-articlesbylanguage',
  templateUrl: './articlesbylanguage.component.html',
  styleUrls: ['./articlesbylanguage.component.css']
})
export class ArticlesbylanguageComponent implements OnInit {

  constructor(private fileService: FileservicesService, private articleService: ArticleserviceService) { }
  language = history.state.data;
  articles$: CompleteArticle[] = [];

  convertDataToImage(image: any) {
    return this.fileService.convertBlobToImage(image.data); 
  }
  ngOnInit(): void {
    console.log("recovering parent state:" + this.language)
    this.articleService.getArticlesByLanguage(this.language).subscribe((list) => {
    console.log(list)
    this.articles$ = list as CompleteArticle[]  ;
    console.log("articles:"+ JSON.stringify(this.articles$))
  })
}
}
