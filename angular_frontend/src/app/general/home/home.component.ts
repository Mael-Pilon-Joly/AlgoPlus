import { Component, OnInit,Input  } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
 
  website_mission_section1!: string;
  website_mission_section2!: string;
  website_mission_section3!: string;

  ngOnInit(){
  }
  

}


