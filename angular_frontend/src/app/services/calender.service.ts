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

   createEvent(title:String, start: string, end:string, startStr: string, endStr: string, allDay: Boolean) {
    console.log(title, start, end, startStr, endStr)
    return this.http.post<any[]>(baseUrl +`/event?title=${title}&start=${start}&end=${end}&startStr=${startStr}&endStr=${endStr}&allday=${allDay}`,  {withCredentials:true})
   }

}