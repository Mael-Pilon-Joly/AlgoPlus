import { Component, OnInit, ViewChild, ElementRef, Injectable } from '@angular/core';
import * as ace from 'ace-builds'; 
import 'ace-builds/src-noconflict/mode-javascript';
import 'ace-builds/src-noconflict/theme-github';
import 'ace-builds/src-noconflict/ext-language_tools';
import 'ace-builds/src-noconflict/ext-beautify';
import { ExerciseService } from 'src/app/services/exercise.service';
import { ActivatedRoute } from '@angular/router';
import { Exercise } from 'src/app/models/exercise.model';
import { FileservicesService } from 'src/app/services/fileservices.service';

const THEME = 'ace/theme/github'; 
const LANG = 'ace/mode/javascript';

interface description {
  v: string;
  language: string;
  tag: string;
}
@Injectable()
@Component({
  selector: 'app-ide',
  templateUrl: './ide.component.html',
  styleUrls: ['./ide.component.css']
})
export class IdeComponent implements OnInit {

  // Program variables
  languageId:number = 0;
  languageName = "";
  languageVersion ="";
  runOutput = "";
  imports=""
  stdIn = "1 2 3";
  pos = "";
  errorLanguageAndVersion = false;

  language:any[] =  [
    {id:0, val:"Java"},
    {id:1, val:"Python2"},
    {id:2, val:"Python3"},
    {id:3, val:"C++"},
    {id:4, val:"C"},
    {id:5, val:"SQL"},
    {id:6, val:"PHP"},
    {id:7, val:"Nodejs"},
  ]


  version:Array<{id: number; value: Array<{v: string; language: string, tag:any}>}>  = [
    {id:0, value:[{v: "0", language:"java", tag:"1.8"}, {v: "3",language:"java", tag:"11"}, {v: "4",language:"java", tag:"17"}]},
    {id:1, value:[{v: "0", language:"python2", tag:"2.7.11"}]},
    {id:2, value:[{v: "0", language:"python3", tag:"3.5.1"}]},
    {id:3, value:[{v: "0", language:"cpp", tag:"5.3.0"}, {v: "2", language:"cpp", tag:"7.2.0"}, {v: "5", language:"cpp", tag:"GCC 11.1.0"}]},
    {id:4, value:[{v: "0", language:"c", tag:"5.3.0"}, {v: "3", language:"c", tag:"8.1.0"}, {v: "4", language:"c", tag:"9.1.0"}, {v: "5", language:"c", tag:"11.1.0"}]},
    {id:5, value:[{v: "0", language:"sql", tag:"SQLite 3.9.2"}, {v: "4", language:"sql", tag:"3.37.0"}]},
    {id:6, value:[{v: "0", language:"php", tag:"5.6.16"}, {v: "1", language:"php", tag:"7.1.11"}, {v: "4", language:"php", tag:"8.0.13"}]},
    {id:7, value:[{v: "0", language:"nodejs", tag:"6.3.1"},{v: "1", language:"nodejs", tag:"9.2.0"}, {v: "3", language:"nodejs", tag:"12.11.1"}]}
  ]

  requestBody = {
    script: "",
    language: "",
    versionIndex: "",
    stdIn: "ab12cd!$_98dh[]()35"
  }

  challenge: Exercise = {
    id: "",
    title:  "",
    username:  "",
    explanation: "",
    image:  undefined,
    solutions: new Map<string,string>()
  }

  image: any;
  tests= false;
  successTest1 = false;
  successTest2 = false;
  successTest3 = false;
  successTest4 = false;
  failureTest1 = false;
  failureTest2 = false;
  failureTest3 = false;
  failureTest4 = false;

  @ViewChild('codeEditor')
  codeEditorElmRef: ElementRef | undefined;
  private codeEditor: ace.Ace.Editor | undefined;
  private editorBeautify: any;

  constructor(private exerciseService: ExerciseService, private fileservices: FileservicesService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    console.log("return:" +  JSON.stringify(this.route.snapshot.paramMap))
    this.pos = this.route.snapshot.paramMap.get('id')!;
    console.log("position" + this.pos)
    var id: number = +this.pos;
    this.challenge.id = this.pos;
    this.exerciseService.fetchExerciseById(id).then( res => {
    this.challenge.image = res.image
    if(this.challenge.image) {
      this.image = this.fileservices.convertBlobToImage(res.image.data);
      }
    this.challenge.title = res.title;
    this.challenge.explanation = res.explanation
    this.challenge.solutions = res.solutions
    this.challenge.username = res.username
    console.log(JSON.stringify("this challenge" + JSON.stringify(this.challenge)))
    }
    ).catch (error=> {
      console.log(error)
    })
}

ngAfterViewInit() {
  ace.require('ace/ext/language_tools');
  console.log(this.codeEditorElmRef)
  const element = this.codeEditorElmRef!.nativeElement;
  const editorOptions = this.getEditorOptions();
  this.editorBeautify = ace.require('ace/ext/beautify');
  this.codeEditor = ace.edit(element, editorOptions);
  this.codeEditor.setTheme(THEME);
  this.codeEditor.getSession().setMode(LANG);
  this.codeEditor.setShowFoldWidgets(true);
  console.log(this.codeEditor)
}

public beautifyContent() {
  if (this.codeEditor && this.editorBeautify) {
     const session = this.codeEditor.getSession();
     this.editorBeautify.beautify(session);
  }
}

public getCode() {
  const code = this.codeEditor!.getValue();
  console.log(code)
  return code;
}

public clear() {
  this.codeEditor?.setValue("")
}

public test() {
  if (this.languageName == "" || this.languageVersion == "") {
    this.errorLanguageAndVersion = true;
  } else {
  this.tests = true;
  this.runTest1();
  }
}

public allTestSucceed(){
  this.tests = false;
  this.successTest1 = false;
  this.successTest2 = false;
  this.successTest3 = false;
  this.successTest4 = false;
  this.failureTest1 = false;
  this.failureTest2 = false;
  this.failureTest3 = false;
  this.failureTest4 = false;
}

public failedTest() {
  this.tests = false;
  this.successTest1 = false;
  this.successTest2 = false;
  this.successTest3 = false;
  this.successTest4 = false;
  this.failureTest1 = false;
  this.failureTest2 = false;
  this.failureTest3 = false;
  this.failureTest4 = false;
}

async runTest1(){

console.log(this.challenge.solutions)
var key =  Object.keys(this.challenge.solutions)[0]; 
var map =  new Map(Object.entries(this.challenge.solutions));         
var val = map.get(key)
console.log("test 1"+ key+ "," +val)
 this.errorLanguageAndVersion = false;
// running the code
this.requestBody.stdIn = key;
this.requestBody.script = this.getCode();
if (this.requestBody.script != "" && this.requestBody.language != "" && this.requestBody.versionIndex!="") {
  console.log(this.requestBody)
await this.exerciseService.getOutputFromCode(this.requestBody).then(res => {
  console.log(res)
  var result = res.output;
  // comparing result
  if(result.trim() == val.trim()) {
  this.successTest1 = true;
  this.runOutput = ("Expected output:" + val + "\n" + "Actual output:" + result)
  setTimeout(() => 
{
  this.runTest2();
},
3000); 
  } else {
  this.failureTest1 = true;
  this.runOutput = ("Expected output:" + val + "\n" + "Actual output:" + result)
  setTimeout(() => 
{
  this.failedTest();
},
3000); 
  }
}). catch (error => {
  console.log(error)
})
}

}

async runTest2(){
var key = Object.keys(this.challenge.solutions)[1];                
var map =  new Map(Object.entries(this.challenge.solutions));         
var val = map.get(key)
console.log("test 2"+ key+ "," +val)
// running the code
this.requestBody.stdIn = key;
this.requestBody.script = this.getCode();
if (this.requestBody.script != "" && this.requestBody.language != "" && this.requestBody.versionIndex!="") {
  console.log(this.requestBody)
await this.exerciseService.getOutputFromCode(this.requestBody).then(res => {
  console.log(res)
  var result = res.output;
  // comparing result
  if(result.trim() == val.trim()) {
  this.successTest2 = true;
  this.runOutput = ("Expected output:" + val + "\n" + "Actual output:" + result)

  setTimeout(() => 
{
  this.runTest3();
},
3000); 
  } else {
  this.failureTest2 = true;
  this.runOutput = ("Expected output:" + val + "\n" + "Actual output:" + result)
  setTimeout(() => 
{
  this.failedTest();
},
3000); 
  }
}). catch (error => {
  console.log(error)
})
}
}

async runTest3(){
  var key = Object.keys(this.challenge.solutions)[2];                
  var map =  new Map(Object.entries(this.challenge.solutions));         
  var val = map.get(key)
  console.log("test 3"+ key+ "," +val)
  // running the code
this.requestBody.stdIn = key;
this.requestBody.script = this.getCode();
if (this.requestBody.script != "" && this.requestBody.language != "" && this.requestBody.versionIndex!="") {
  console.log(this.requestBody)
await this.exerciseService.getOutputFromCode(this.requestBody).then(res => {
  console.log(res)
  var result = res.output;
  // comparing result
  if(result.trim() == val.trim()) {
  this.successTest3 = true;
  this.runOutput = ("Expected output:" + val + "\n" + "Actual output:" + result)

  setTimeout(() => 
{
  this.runTest4();
},
3000);
  } else {
  this.failureTest3 = true;
  this.runOutput = ("Expected output:" + val + "\n" + "Actual output:" + result)
  setTimeout(() => 
{
  this.failedTest();
},
3000); 
  }
}). catch (error => {
  console.log(error)
})
} 
}

async runTest4(){
var key = Object.keys(this.challenge.solutions)[3];               
var map =  new Map(Object.entries(this.challenge.solutions));         
var val = map.get(key)
// running the code
this.requestBody.stdIn = key;
this.requestBody.script = this.getCode();
if (this.requestBody.script != "" && this.requestBody.language != "" && this.requestBody.versionIndex!="") {
  console.log(this.requestBody)
await this.exerciseService.getOutputFromCode(this.requestBody).then(res => {
  console.log(res)
  var result = res.output;
  // comparing result
  if(result.trim() == val.trim()) {
  this.successTest4 = true;
  this.runOutput = ("Expected output:" + val + "\n" + "Actual output:" + result)

  setTimeout(() => 
  {
    this.allTestSucceed();
  },
  3000); 
    
  } else {
  this.failureTest4 = true;
  this.runOutput = ("Expected output:" + val + "\n" + "Actual output:" + result)
  setTimeout(() => 
{
  this.failedTest();
},
3000); 
  }
}). catch (error => {
  console.log(error)
})
}
}



private getEditorOptions(): Partial<ace.Ace.EditorOptions> & { enableBasicAutocompletion?: boolean; } {
  const basicEditorOptions: Partial<ace.Ace.EditorOptions> = {
      highlightActiveLine: true,
      minLines: 14,
      maxLines: Infinity,
  };

  const extraEditorOptions = {
      enableBasicAutocompletion: true
  };
  const margedOptions = Object.assign(basicEditorOptions, extraEditorOptions);
  return margedOptions;

}

selectChangeHandler (event: any, val:any, i:any) {
  console.log(val + " " + i)
  this.languageName = val;
  this.languageId = i;
  this.languageVersion = "";
}

selectChangeHandlerVersion (event: any, v:any, language:any, tag:any) {
  console.log(v, language)
  this.requestBody.language = language;
  this.requestBody.versionIndex  = v;
  this.languageVersion = tag;
  console.log(this.requestBody)
}

async runCode() {
  this.errorLanguageAndVersion = false;
if (this.languageName == "" || this.languageVersion == "") {
  this.errorLanguageAndVersion = true;
}
this.requestBody.stdIn = this.stdIn;
this.requestBody.script = this.getCode();
if (this.requestBody.script != "" && this.requestBody.language != "" && this.requestBody.versionIndex!="") {
  console.log(this.requestBody)
await this.exerciseService.getOutputFromCode(this.requestBody).then(res => {
  console.log(res)
  this.runOutput = res.output;
}). catch (error => {
  console.log(error)
})
} 
}
}
