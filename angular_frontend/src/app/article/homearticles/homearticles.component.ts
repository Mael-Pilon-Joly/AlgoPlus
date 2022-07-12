import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-homearticles',
  templateUrl: './homearticles.component.html',
  styleUrls: ['./homearticles.component.css']
})
export class HomearticlesComponent implements OnInit {

  constructor(private router: Router) { }

  writeArticle() {
    this.router.navigate(['/writearticle']);
  }
  ngOnInit(): void {
  }

}
