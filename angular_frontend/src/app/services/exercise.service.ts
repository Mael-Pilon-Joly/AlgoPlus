import { formatCurrency } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CompileRequest } from '../models/compileRequest.model';
import { Exercise } from '../models/exercise.model';

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

  async createExercise(exercise: Exercise, image: File) {
    const obj = Object.fromEntries(exercise.solutions);
    const formData: FormData = new FormData();
    formData.append('image',image);
    formData.append('title', exercise.title)
    formData.append('explanation', exercise.explanation)
    formData.append('solutions', JSON.stringify(obj))
    console.log(formData)
    return new Promise ((resolve,reject) => this.http.post( baseUrl + "/exercise", formData ,  {
      withCredentials: true
    }).subscribe(
      (response:any) => {
        resolve(response);
      }, error => {
        reject({error: error.error});
      })
      )}

  async fetchExercises(): Promise<any> {
    return new Promise ((resolve,reject) => this.http.get( baseUrl + "/exercices" ,  {
      withCredentials: true
    }).subscribe(
      (response:any) => {
        resolve(response);
      }, error => {
        reject({error: error.error});
      })
      )}
  }

