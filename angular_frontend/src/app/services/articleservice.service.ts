import { HttpClient, HttpEvent, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
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
    const formData: FormData = new FormData();
      formData.append('image',image);
      formData.append('username', article.username)
      formData.append('title', article.title)
      formData.append('content', article.content)
      formData.append('language', article.language);
      console.log(JSON.stringify(formData))
    return new Promise ((resolve,reject) => this.http.post( baseUrl + "/article", formData ,  {
      withCredentials: true
    }).subscribe(
      (response:any) => {
        resolve(response);
      }, error => {
        reject({error: error.error});
      })
    )
  }

  getArticlesByLanguage(language: string): Observable<any>{
    return this.http.get<any>(baseUrl +`/articlesbylanguage?language=${language}`)
  }

  getArticles(): Observable<CompleteArticle[]> {
    return this.http.get<CompleteArticle[]>(baseUrl +"/articles", {withCredentials:true})
  }
}
