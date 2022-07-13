import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AllCompleteArticle } from 'src/app/models/allArticlesResponse.model';
import { CompleteArticle } from 'src/app/models/completearticle.model';
import { ArticleserviceService } from 'src/app/services/articleservice.service';

@Component({
  selector: 'app-homearticles',
  templateUrl: './homearticles.component.html',
  styleUrls: ['./homearticles.component.css']
})
export class HomearticlesComponent implements OnInit {
  
  articles$: AllCompleteArticle = new AllCompleteArticle;

  constructor(private router: Router, private articleService: ArticleserviceService) { }

  writeArticle() {
    this.router.navigate(['/writearticle']);
  }

  ngOnInit(): void {
   this.articleService.getArticles().subscribe((list) => {
    this.articles$ = list as AllCompleteArticle;
    this.articles$.article = this.articles$.article.slice(-7)
    console.log(this.articles$.article)
  })
  }


}
