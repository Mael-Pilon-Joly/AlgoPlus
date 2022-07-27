import { EventEmitter, Injectable, Output } from '@angular/core';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RequestResponseUser } from '../models/requestresponselogin.model';
import { User } from '../models/user.model';
import { faLeaf } from '@fortawesome/free-solid-svg-icons';
import { SignUpResponse } from '../models/signupresponse.model';
import { LoginResponse } from '../models/loginresponse.model';
import { Roles } from '../models/roles.model';

const baseUrl = 'http://143.198.169.178:8080/api/auth';
const loggedInUrl = 'http://143.198.169.178/:8080/api/loggedin';
const userUrl = 'http://143.198.169.178/:8080/api/user'

@Injectable({
  providedIn: 'root'
})

export class ApiService {
  @Output() getLogged: EventEmitter<any> = new EventEmitter();
  @Output() getUser: EventEmitter<any> = new EventEmitter();
  @Output() getRoles: EventEmitter<any> = new EventEmitter();

  [x: string]: any;
  isLoggedIn = false;
  user: User = {
    id: 0,
    username: "",
    email: "",
    roles: []
  }
  requestResponse: User ={}


  constructor(private http: HttpClient) { }

  getUserValue() {
    return this.requestResponse;
  }

  setValue(roles: Roles[]){
    console.log("settig user roles globally..." + JSON.stringify(roles))
    this.user.roles = roles;
    var role;
    if( roles.filter(e => e.name === 'ROLE_ADMIN').length > 0 ) {
      role="admin";
    }

    if( roles.filter(e => e.name === 'ROLE_USER').length > 0 ) {
      role="user";
    }
  
    this.getRoles.emit(role)
   }
 
   getValue(){
    return  this.user.roles;
   }

  async login(data: any, rememberme:boolean): Promise<User> {
    
    return new Promise ((resolve,reject) =>  this.http.post<LoginResponse>(baseUrl + "/signin", data, {
      withCredentials: true
    }).subscribe(async data => {
      this.isLoggedIn = true;
      this.getLogged.emit(this.isLoggedIn);
      console.log(JSON.stringify(data))
      console.log("login, rememberme", rememberme)
      if (data.sessionToken && !rememberme) {
      sessionStorage.setItem('token', data.sessionToken)
      }
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
    this.http.post("http://143.198.169.178/8080/logout", null, {
      withCredentials: true
    }).subscribe(data => {
      this.isLoggedIn = false;
      sessionStorage.setItem('token', "")
      this.getLogged.emit(this.isLoggedIn);
      window.location.reload()
    },
      error => {
        console.log(error);
      });
  }

  async getProfil(): Promise<User> {
   return new Promise ((resolve,reject) => this.http.get("http://143.198.169.178/8080/api/loggedin/profil", {
      withCredentials: true
    }).subscribe(response => {
      this.isLoggedIn = true;
      this.getLogged.emit(this.isLoggedIn);
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

  async signUp(username: string, email: string,role: any, password: string): Promise<SignUpResponse> {
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

  async passwordRequest(email: string): Promise<SignUpResponse> {
    return new Promise ((resolve,reject) => this.http.post(userUrl + "/resetpassword", {email},  {
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

  async updatePassword(password:string, token: any): Promise<SignUpResponse> {
    return new Promise ((resolve,reject) => this.http.post(userUrl + "/savepassword", {password, token},  {
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

   updateProfile(file: File, username:string, type: string): Observable<HttpEvent<any>> {
      const formData: FormData = new FormData();
      formData.append('file', file);
      formData.append('username', username);
      formData.append('typeofrequest', type);
      console.log(JSON.stringify(formData))
      const req = new HttpRequest('PUT', `${loggedInUrl}/updateprofile`, formData, {
        reportProgress: true,
        responseType: 'json',
        withCredentials: true
      });
      return this.http.request(req);
    }

    receiveMessage(source:String, name:String, message:String){
      return this.http.post("http://143.198.169.178/8080/api/mailbox/receive", {source, name, message},  {
        withCredentials: true
      })
    }

    async validateAdminRole(): Promise<any> {
       return new Promise((resolve,reject) =>  this.http.post("http://143.198.169.178/8080/api/admin/auth", {
        withCredentials: true
      }).subscribe(
        (response:any) => {
          resolve(response);
        }, error => {
          reject({error: error.error});
        })
      )
    }

getUserByUsername(username:string): Observable<any> {
      return this.http.get<any>(`http://143.198.169.178/8080/api/admin/user?username=${username}`, {withCredentials:true})
    }

async updateUserPoints(username:string, points:number): Promise<any> {
  return new Promise((resolve,reject) => this.http.put<any>(`http://143.198.169.178/8080/api/admin/userpoints?username=${username}&points=${points}`, {withCredentials:true}).subscribe(
    (response:any) => {
      resolve(response);
    }, error => {
      reject({error: error.error});
    })
  )
    }

async updateUserStatus(username:string): Promise<any> {
  return new Promise((resolve,reject) => this.http.put<any>(`http://143.198.169.178/8080/api/admin/userlocked?username=${username}`, {withCredentials:true}).subscribe(
    (response:any) => {
      resolve(response);
    }, error => {
      reject({error: error.error});
    })
  )
    }

    getUsers(): Observable<any[]> {
      return this.http.get<any[]>(`http://143.198.169.178/8080/api/user/ranking`, {withCredentials:true})
    }

    async messageBetweenUsers(username: string, source:string, dest:string, message:string): Promise<any> {
      return new Promise ((resolve,reject) => this.http.post(loggedInUrl + `/messagebetweenuser?username=${username}&source=${source}&dest=${dest}&message=${message}`,  {
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
  
  
 

  



