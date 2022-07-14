import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CompleteArticle } from 'src/app/models/completearticle.model';
import { ArticleserviceService } from 'src/app/services/articleservice.service';

@Component({
  selector: 'app-homearticles',
  templateUrl: './homearticles.component.html',
  styleUrls: ['./homearticles.component.css']
})
export class HomearticlesComponent implements OnInit {
  
  articles$: CompleteArticle[] = [];

  constructor(private router: Router, private articleService: ArticleserviceService) { }

  writeArticle() {
    this.router.navigate(['/writearticle']);
  }

  ngOnInit(): void {
   this.articleService.getArticles().subscribe((list) => {
    this.articles$ = list as CompleteArticle[];
    console.log("articles:"+ JSON.stringify(this.articles$))
    this.articles$ = this.articles$.slice(-7)
  
  })
  }


}
