import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/angular';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { DatePipe } from '@angular/common';

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

  Events: any[] = [
    {title: 'Develop a successful Wearable Tech Entrepreneur Start Up Buisness.', allDay: false, start: "2022-07-24", startStr: "2022-07-24 13:00:00.000",  endStr: "2022-07-24 15:00:00.000"},
    {title: 'test',  allDay: true, date: "2022-07-24"}
  ];
  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    events: this.Events,
    eventClick: this.handleDateClick.bind(this) 
  }

  config = {
    animated: true
  }
  @ViewChild('template') template!: string;

  constructor(private httpClient: HttpClient, private modalService: BsModalService, private datePipe: DatePipe) {
   }

  onDateClick(res: { dateStr: string; }) {
    alert('Clicked on date : ' + res.dateStr)
  }

  handleDateClick(arg:any) {
    console.log(arg)
    this.title =arg.event._def.title;
    this.start= arg.event._def.extendedProps.startStr;
    this.end= arg.event._def.extendedProps.endStr;
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

    }
  

}
