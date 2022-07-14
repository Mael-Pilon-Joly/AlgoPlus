import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Article } from '../models/article.model';
import { CompleteArticle } from '../models/completearticle.model';

const baseUrl = 'http://localhost:8080/api/article';

@Injectable({
  providedIn: 'root'
})

export class ArticleserviceService {

  constructor(private http: HttpClient) { }

  async createArticle(article: Article, image:File): Promise<any> {
    console.log(article, image)
    const formData: FormData = new FormData();
      formData.append('username', article.username);
      formData.append('title', article.title);
      formData.append('content', article.content);
      formData.append('language', article.language);
      formData.append('image',image);
    return new Promise ((resolve,reject) => this.http.post( baseUrl + "/article", {formData},  {
      withCredentials: true
    }).subscribe(
      (response:any) => {
        console.log("response"+response)
        resolve(response);
      }, error => {
        console.log("error"+JSON.stringify(error.error))
        reject({error: error.error});
      })
    )
  }

  getArticles(): Observable<CompleteArticle[]> {
    return this.http.get<CompleteArticle[]>(baseUrl +"/articles", {withCredentials:true})
  }
}
