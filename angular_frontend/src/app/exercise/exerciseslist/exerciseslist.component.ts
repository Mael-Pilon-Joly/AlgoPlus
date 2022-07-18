import { Component, OnInit, ViewChild } from '@angular/core';
import {MatTableModule} from '@angular/material/table';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ExerciseService } from 'src/app/services/exercise.service';

export interface ExerciseElement {
  position: number;
  title: string;
  author: string;
  resolve: string;
}

@Component({
  selector: 'app-exerciseslist',
  templateUrl: './exerciseslist.component.html',
  styleUrls: ['./exerciseslist.component.css']
})

export class ExerciseslistComponent implements OnInit {

  exercises: ExerciseElement[] = [
    
  ]; 
  
  constructor(private translate: TranslateService, private exerciseServices: ExerciseService, private router: Router) { }

  startChallenge(position: number){
    console.log("position:" + position)
    this.router.navigate(['/ide',   { id: position } ]);
  }

  ngOnInit(): void {
   this.exerciseServices.fetchExercises().then(res => {
    var fetchedList = res;
    var data = [];
    var i = 0;
    for (; i<fetchedList.length ; i++ ) {
      data.push({position: fetchedList[i].id, title: fetchedList[i].title, author: fetchedList[i].creator_username, resolve:""})
    }
    this.exercises = data;
    console.log("exercises:" + JSON.stringify(this.exercises))

   })
  }
  dataSource = this.exercises;
  displayedColumns: string[] = ['position', 'title', 'author', 'resolve'];

 }

