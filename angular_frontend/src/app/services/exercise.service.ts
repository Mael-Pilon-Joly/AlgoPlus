import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CompileRequest } from '../models/compileRequest.model';

const baseUrl = "http://localhost:8080/api/exercise"

@Injectable({
  providedIn: 'root'
})
export class ExerciseService {

  constructor(private http: HttpClient) { }

  async getOutputFromCode(request: CompileRequest): Promise<any> {
    console.log(request)
    return new Promise ((resolve,reject) => this.http.post( baseUrl + "/compile", request ,  {
      withCredentials: true
    }).subscribe(
      (response:any) => {
        resolve(response);
      }, error => {
        reject({error: error.error});
      })
  )}
}
