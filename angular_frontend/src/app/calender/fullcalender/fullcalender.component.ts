import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/angular';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { DatePipe } from '@angular/common';
import { DataRowOutlet } from '@angular/cdk/table';
import { DataService } from 'src/app/services/calender.service';
import { map, catchError, tap } from 'rxjs/operators';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import frLocale from '@fullcalendar/core/locales/fr';
import { ApiService } from 'src/app/services/apiservices.service';
import { User } from 'src/app/models/user.model';
import { Router } from '@angular/router';
import { calendar } from 'ngx-bootstrap/chronos/moment/calendar';

@Component({
  selector: 'app-fullcalender',
  templateUrl: './fullcalender.component.html',
  styleUrls: ['./fullcalender.component.css']
})
export class FullcalenderComponent implements OnInit {

  user: User = {};
  event:any={};
  eventId: number = 0;
  eventCreator: any;
  modalRef?: BsModalRef;
  titleAddEvent: string="";
  title: string ="";
  start:any;
  end: any;
  startStr = new Date();
  endStr = new Date();
  allDay= false;
  startTime:any;
  endTime:any;
  events1 : any[] = []
  errorTitle=false;
  errorDate=false;
  errorDateValidity = false;
  calendarOptions2$ = this.httpClient
  .get('http://localhost:8080/api/event/events')
  .pipe(
    map((res: any) => {
     console.log(JSON.stringify(res))
     this.calendarOptions.events = res;
     return res;
    })
  );

  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    eventClick: this.handleDateClick.bind(this),
    events: this.events1,
    displayEventTime: false,
    locale: this.translate.currentLang  }

  config = {
    animated: true
  }
  @ViewChild('template') template!: string;
  @ViewChild('calendar') calendar!: ElementRef;

  constructor(private httpClient: HttpClient, private cd: ChangeDetectorRef, private router:Router, public translate: TranslateService, private modalService: BsModalService, private service: ApiService, private datePipe: DatePipe, private eventService: DataService) {
   }

  onDateClick(res: { dateStr: string; }) {
    alert('Clicked on date : ' + res.dateStr)
  }

  handleDateClick(arg:any) {

    console.log(arg)
    this.title =arg.event._def.title;
    var startDate= arg.event._def.extendedProps.startStr;
    this.start = new Date(startDate).toLocaleDateString(this.translate.currentLang,{ day: 'numeric', month: 'short', year: 'numeric' }) + " " + new Date(startDate).toLocaleTimeString(this.translate.currentLang)
    var temp = arg.event._def.extendedProps.endStr;
    var date =new Date(temp).toLocaleDateString(this.translate.currentLang, { day: 'numeric', month: 'short', year: 'numeric' }) + " " + new Date(temp).toLocaleTimeString(this.translate.currentLang)
    console.log (date)
    this.end= date;
    this.modalRef = this.modalService.show(this.template, this.config)
    this.eventCreator = arg.event._def.extendedProps.username_creator;
    this.eventId =  Number(arg.event._def.publicId);
    this.event = arg.event;
  }
 
  ngOnInit(){
    this.user = this.service.getUserValue()!;
    this.translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.calendarOptions.locale = event.lang
    });
    }  

    deleteEvent() {
      this.eventService.deleteEvent(this.eventId).then(res=> {
        console.log(res)
        this.event.remove();
        this.modalService._hideModal(this.template);
      }).catch(error=> {
        console.log(error)
        this.modalService._hideModal(this.template);
      });
    }


    onItemChange(event:any) {
      this.allDay =! this.allDay;
    }

    changeEndTime(event: any){
    console.log(event);
    }
    
    addEvent() {
      var before1;
      var start = new Date(this.startStr)
      const offset =start.getTimezoneOffset()
      start = new Date(start.getTime() - (offset*60*1000))
      before1 = new Date(start).toISOString().split('T')[0]
      if(this.startTime != undefined) {
      this.startTime = this.convertTime12to24(this.startTime)
      this.startTime = before1 +"T"+ this.startTime
      }
      var before2;
      var end = new Date(this.endStr)
      end= new Date(end.getTime() - (offset*60*1000))
      before2 = new Date(end).toISOString().split('T')[0]
      if(this.endTime != undefined) {
      this.endTime = this.convertTime12to24(this.endTime)
      this.endTime = before2 +"T"+ this.endTime
      }
      if(this.allDay) {
      this.endTime = undefined
      this.end = undefined
      this.startTime = undefined;
      }
  
      this.errorTitle = false;
      this.errorDate = false;
      this.errorDateValidity = false;
      var date1 = new Date(this.startTime);
      var date2 = new Date(this.endTime);
      if(date1>date2) {
      this.errorDateValidity = true;
      } else if (this.title ==undefined) {
      this.errorTitle = true;
      } else if (!this.allDay && (this.startStr==undefined || this.endStr==undefined || this.startTime ==undefined || this.endTime ==undefined  )) {
      this.errorDate = true;
      } else {
      console.log("{}:" + this.startTime+","+this.endTime)
      this.eventService.createEvent(this.titleAddEvent, before1, before2, this.startTime, this.endTime, this.allDay).subscribe( res=> 
        {
          this.calendarOptions2$ = this.httpClient
        .get('http://localhost:8080/api/event/events')
        .pipe(
         map((res: any) => {
         console.log(JSON.stringify(res))
         this.calendarOptions.events = res;
         return res;
         })
      );
        });
  
    }
    }

   convertTime12to24 = (time12h:string) => {
    console.log(time12h)
      const [time, modifier] = time12h.split(' ');
      console.log(time+":"+modifier)

      let [hours, minutes] = time.split(':');
      console.log(hours+":"+minutes)
      if (hours === '12') {
        hours = '00';
      }

      if(hours.length == 1){
        hours = '0'+ hours;
      }
    
      if (modifier === 'PM') {
        hours = (parseInt(hours, 10) + 12).toString();
      }
    
      return `${hours}:${minutes}:00`;
    }
  

}
