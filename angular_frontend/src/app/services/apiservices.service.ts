import { EventEmitter, Injectable, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RequestResponseUser } from '../models/requestresponselogin.model';
import { User } from '../models/user.model';
import { faLeaf } from '@fortawesome/free-solid-svg-icons';

const baseUrl = 'http://localhost:8080/api/auth';
const loggedInUrl = 'http://localhost:8080/api/loggedin';

@Injectable({
  providedIn: 'root'
})

export class ApiService {
  @Output() getLogged: EventEmitter<any> = new EventEmitter();

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

  login(data: any): RequestResponseUser {
    this.http.post(baseUrl + "/signin", data, {
      withCredentials: true
    }).subscribe(data => {
      this.isLoggedIn = true;
      this.getLogged.emit(this.isLoggedIn);
      this.getProfil()
    },
      error => {
        console.log(error);
        return null;
      });
    return this.requestResponse;
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

  getProfil(): void {
    this.http.get(loggedInUrl + "/profil", {
      withCredentials: true
    }).subscribe(response => {
      console.log(response);
      var res = response;
      if (this.requestResponse.user) {
        console.log("success accessing res.data")
        this.requestResponse = res;

      } else {
        console.log("error getting requestResponseInitialState")
      }

      console.log(this.requestResponse)
    },
      error => {
        console.log(error);
      });
  }
}

