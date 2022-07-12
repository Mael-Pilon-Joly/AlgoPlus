import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Article } from '../models/article.model';

const baseUrl = 'http://localhost:8080/api/article';

@Injectable({
  providedIn: 'root'
})

export class ArticleserviceService {

  constructor(private http: HttpClient) { }

  async createArticle(article: Article, image:File): Promise<any> {
    return new Promise ((resolve,reject) => this.http.post( baseUrl + "/resetpassword", {article, image},  {
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
}
