import { EventEmitter, Injectable, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RequestResponseUser } from '../models/requestresponselogin.model';
import { User } from '../models/user.model';
import { faLeaf } from '@fortawesome/free-solid-svg-icons';
import { SignUpResponse } from '../models/signupresponse.model';

const baseUrl = 'http://localhost:8080/api/auth';
const loggedInUrl = 'http://localhost:8080/api/loggedin';

@Injectable({
  providedIn: 'root'
})

export class ApiService {
  @Output() getLogged: EventEmitter<any> = new EventEmitter();
  @Output() getUser: EventEmitter<any> = new EventEmitter();

  [x: string]: any;
  isLoggedIn = false;
  user: User = {
    id: 0,
    username: "",
    email: "",
    roles: []
  }
  requestResponse: RequestResponseUser = {
    user: this.user
  }


  constructor(private http: HttpClient) { }

  async login(data: any): Promise<RequestResponseUser> {
    return new Promise ((resolve,reject) =>  this.http.post(baseUrl + "/signin", data, {
      withCredentials: true
    }).subscribe(async data => {
      this.isLoggedIn = true;
      this.getLogged.emit(this.isLoggedIn);
      await this.getProfil().then(res=> {
          console.log("success accessing res.data")
          this.requestResponse = res;
          console.log(this.requestResponse)
          this.getUser.emit(this.requestResponse);
          resolve(res);
      })
    },
      error => {
        console.log(error);
        reject({error: error});
      }));
  }

  logout(): void {
    this.http.post("http://localhost:8080/logout", null, {
      withCredentials: true
    }).subscribe(data => {
      this.isLoggedIn = false;
      this.getLogged.emit(this.isLoggedIn);
    },
      error => {
        console.log(error);
      });
  }

  async getProfil(): Promise<RequestResponseUser> {
   return new Promise ((resolve,reject) => this.http.get("http://localhost:8080/api/loggedin/profil", {
      withCredentials: true
    }).subscribe(response => {
      console.log(response);
      this.requestResponse = response;
      console.log(this.requestResponse)
      resolve(response);
    },
      error => {
        console.log(error);
        reject({error: error});
      }));
  }

  async signUp(username: string, email: string,role: string[], password: string): Promise<SignUpResponse> {
    return new Promise ((resolve,reject) => this.http.post(baseUrl + "/signup", {username, email, password, role},  {
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

