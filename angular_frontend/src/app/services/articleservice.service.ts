import { HttpClient, HttpEvent, HttpParams, HttpRequest } from '@angular/common/http';
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
    const params = new HttpParams()
   .set('username', article.username)
   .set('title', article.title)
   .set('content', article.content)
   .set('language', article.language);
    console.log(article, image)
    const formData: FormData = new FormData();
      formData.append('image',image);
      let username = article.username;
      let title = article.title;
      let content = article.content;
      let language = article.language;
    return new Promise ((resolve,reject) => this.http.post( baseUrl + "/article", {params, formData} ,  {
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
