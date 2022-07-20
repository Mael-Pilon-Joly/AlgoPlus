import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {DayPilot} from "@daypilot/daypilot-lite-angular";
import {HttpClient} from "@angular/common/http";

const baseUrl = 'http://localhost:8080/api/event';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  constructor(private http : HttpClient){
  }

    getEvents():Observable<any[]> {
    return this.http.get<any[]>(baseUrl +"/events", {withCredentials:true})
  }

}