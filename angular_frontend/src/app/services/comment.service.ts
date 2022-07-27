import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CommentModel } from '../models/comment.model';

const baseUrl = 'http://143.198.169.178/8080/api/comments/'

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) { }

  getAllCommentsForArticle(articleId: number): Observable<CommentModel[]> {
    return this.http.get<CommentModel[]>(baseUrl +`comments/?articleId=${articleId}`, {withCredentials:true})
  }

  postComment(articleId: number, content:string): Observable<CommentModel> {
    return this.http.post<CommentModel>(baseUrl +`comment?articleId=${articleId}&comment=${content}`, {withCredentials:true})
  }

  deleteComment(commentId: number): Observable<any> {
    console.log("deleting comment...")
    return this.http.delete<any>(baseUrl +`comment?id=${commentId}`, {withCredentials:true})
  }
}
