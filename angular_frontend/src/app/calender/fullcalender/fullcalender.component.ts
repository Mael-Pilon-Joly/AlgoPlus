import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/angular';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { DatePipe } from '@angular/common';
import { DataRowOutlet } from '@angular/cdk/table';
import { DataService } from 'src/app/services/calender.service';
import { map, catchError, tap } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-fullcalender',
  templateUrl: './fullcalender.component.html',
  styleUrls: ['./fullcalender.component.css']
})
export class FullcalenderComponent implements OnInit {

  modalRef?: BsModalRef;
  title: string ="";
  start:any;
  end: string="";
  startStr = new Date();
  endStr = new Date();
  allDay= false;
  startTime:any;
  endTime:any;
  events1 : any[] = []
  calendarOptions2$ = this.httpClient
  .get('http://localhost:8080/api/event/events')
  .pipe(
    map((res: any) => {
     console.log(res)
     this.calendarOptions.events = res;
     return res;
    })
  );

  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    eventClick: this.handleDateClick.bind(this),
    events: this.events1,
    displayEventTime: false
  }

  config = {
    animated: true
  }
  @ViewChild('template') template!: string;

  constructor(private httpClient: HttpClient, private translate: TranslateService, private modalService: BsModalService, private datePipe: DatePipe, private eventService: DataService) {
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
  }
 
  ngOnInit(){
    }  


    onItemChange(event:any) {
      this.allDay =! this.allDay;
    }

    changeEndTime(event: any){
    console.log(event);
    }

    addEvent() {
      console.log(this.datePipe.transform(this.startStr,"yyyy-MM-dd"));
      console.log(this.datePipe.transform(this.endStr,"yyyy-MM-dd"));
      console.log(this.calendarOptions.events)
    }
  

}
