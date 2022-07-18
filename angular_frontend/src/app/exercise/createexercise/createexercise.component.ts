import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Exercise } from 'src/app/models/exercise.model';
import { User } from 'src/app/models/user.model';
import { ApiService } from 'src/app/services/apiservices.service';
import { ExerciseService } from 'src/app/services/exercise.service';

@Component({
  selector: 'app-createexercise',
  templateUrl: './createexercise.component.html',
  styleUrls: ['./createexercise.component.css']
})
export class CreateexerciseComponent implements OnInit {

  selectedFiles?: FileList;
  currentFile?: File;
  image?: File | undefined;
  fileInfos?: Observable<any>;
  errorTitle = false;
  errorExplanation = false;
  errorSolutions = false;
  errorGeneral = false;
  input1="";
  input2="";
  input3="";
  input4="";
  output1="";
  output2="";
  output3="";
  output4="";
  user?: User;

  exercise: Exercise = {
    id: "",
    username: "",
    title: "",
    explanation:"",
    solutions: new Map<string,string>()
  }

  
  constructor(private apiservices: ApiService, private exerciseservices: ExerciseService, private http: HttpClient, private router: Router, private route: ActivatedRoute) { }


  fileChange(event:any) {
    let fileList: FileList = event.target.files;
    if(fileList.length > 0) {
        this.image = fileList[0];
    }
  }

 async createExercise(): Promise<void> {
  console.log("create exercise triggered...")
  console.log(this.input1,this.input2,this.input3,this.input4,this.output1,this.output2,this.output3,this.output4)
  this.errorTitle = false;
  this.errorExplanation = false;
  this.errorSolutions = false;
  this.errorGeneral = false;
  this.exercise.solutions.clear();
  if (this.input1 != "" && this.output1!= "") {
    this.exercise.solutions.set(this.input1, this.output1);
  }
  if (this.input2 != "" && this.output2!= "") {
    this.exercise.solutions.set(this.input2, this.output2);
  }
  if (this.input3 != "" && this.output3!= "") {
    this.exercise.solutions.set(this.input3, this.output3);
  }
  if (this.input4 != "" && this.output4!= "") {
    this.exercise.solutions.set(this.input4, this.output4);
  }
       if(this.exercise.title == "") {
        this.errorTitle = true;
       } 
       if(this.exercise.explanation.length < 50) {
        this.errorExplanation = true;
       } 
       if(this.exercise.solutions.size != 4) {
        this.errorSolutions = true;
       } 
       console.log(this.user)
       if (!this.errorTitle && !this.errorExplanation && !this.errorSolutions && this.user?.username) {
        this.exercise.image = this.image;
       console.log(this.exercise)
        await this.exerciseservices.createExercise(this.exercise, this.image! ).then((res: any)=> {
          console.log(res);
          this.router.navigate(['/userboard']);
        }).catch ( (error: { error: any; }) => {
          this.errorGeneral = true;
          console.log(JSON.stringify(error.error))
        })
      }
    }


  ngOnInit(): void {
    this.user = this.apiservices.getUserValue();
    console.log(this.user)

}
}



