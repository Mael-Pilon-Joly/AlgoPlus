import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { ApiService } from 'src/app/services/apiservices.service';
import { ArticleserviceService } from 'src/app/services/articleservice.service';
import { CommentService } from 'src/app/services/comment.service';
import { ExerciseService } from 'src/app/services/exercise.service';

@Component({
  selector: 'app-managementboard',
  templateUrl: './managementboard.component.html',
  styleUrls: ['./managementboard.component.css']
})
export class ManagementboardComponent implements OnInit {

  title = "";
  exerciseTitle=""
  username=""
  articles$!:any[]; 
  exercises$!:any[];
  user$!:User;
  displayArticles = false
  displayExercises = false
  displayUser = false
  points = 0;

  constructor(private translateService: TranslateService , private services: ApiService, private articleServices: ArticleserviceService,
              private router:Router, private route: ActivatedRoute, private commentService: CommentService, private exerciseServices: ExerciseService) { }
 
  ngOnInit(): void {
   this.validateAdminRole()
   this.title = this.route.snapshot.paramMap.get('articles')!
   if (this.title!=""){
    this.articleServices.getArticlesByTitle(this.title).subscribe((list)=>{
      this.articles$ = list as any[]
      console.log(this.articles$)
    })
   }
   this.exerciseTitle = this.route.snapshot.paramMap.get('exercise')!
   console.log(this.exerciseTitle)
   if (this.exerciseTitle!=""){
    this.exerciseServices.getExercisesByTitle(this.exerciseTitle).subscribe((list)=>{
      this.exercises$ = list as any[]
      console.log(this.exercises$)
    })
   }   
  this.username = this.route.snapshot.paramMap.get('username')!
   console.log(this.username)
   if (this.username!=""){
    this.services.getUserByUsername(this.username).subscribe((user)=>{
      this.user$ = user as any
      console.log(this.user$)
    })
   }   

  }

async validateAdminRole(): Promise<void> {
    this.services.validateAdminRole().then(res=>{
      console.log(res)
    }).catch(err =>{
      alert("Access denied")
      console.log(err)
      this.router.navigate(['/home']);
    })
  }

deleteComment(id: number) {
    if(confirm(this.translateService.instant('deletecommentconfirmation'))) {
      this.commentService.deleteComment(id).subscribe((res) => {
        console.log(res)
        window.location.reload()
        },
        error => {
          console.log(error)
        },)
      }
  }

async deleteArticle(id: number) {
    if(confirm(this.translateService.instant('deletearticleconfirmation'))) {
      await  this.articleServices.deleteArticle(id).then( res=> {
        console.log(res)
        window.location.reload()
    }).catch (error => {
      console.log(error)
    })
  }
}

async deleteExercise(id: number) {
  if(confirm(this.translateService.instant('deleteexerciseconfirmation'))) {
    await  this.exerciseServices.deleteExercise(id).then( res=> {
      console.log(res)
      window.location.reload()
  }).catch (error => {
    console.log(error)
  })
}
}

handleMinus() {
  this.points--;  
}
handlePlus() {
  this.points++;    
}

async updatePoints(username:string) {
  await this.services.updateUserPoints(username, this.points).then( (res: any)=> {
      console.log(res)
      window.location.reload()
  }).catch ((error: any) => {
    console.log(error)
  })
}

async updateStatus(username:string) {
 await this.services.updateUserStatus(username).then( (res: any)=> {
     console.log(res)
     window.location.reload()
 }).catch ((error: any) => {
   console.log(error)
 })
}
}

