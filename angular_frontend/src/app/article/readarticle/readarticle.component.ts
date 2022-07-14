import { Component, OnInit } from '@angular/core';
import { SafeUrl } from '@angular/platform-browser';
import { FileservicesService } from 'src/app/services/fileservices.service';

@Component({
  selector: 'app-readarticle',
  templateUrl: './readarticle.component.html',
  styleUrls: ['./readarticle.component.css']
})
export class ReadarticleComponent implements OnInit {
  
  constructor(private fileservices: FileservicesService ) { }
  article = history.state.data;
  image: SafeUrl | undefined;


  ngOnInit(): void {
    console.log(this.article)
    if(this.article.image) {
    this.image = this.fileservices.convertBlobToImage(this.article.image.data);
    }
  }

}
