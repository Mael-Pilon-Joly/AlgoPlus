import { Injectable } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class FileservicesService {

  constructor( private sanitizer: DomSanitizer) { }


  convertBlobToImage(data: string): SafeUrl {
     console.log("image data:" + data)
         let objectURL = 'data:image/jpeg;base64,' + data;
        return this.sanitizer.bypassSecurityTrustUrl(objectURL);
  }

  convertBlobToText(data:any): SafeUrl {
    var blob = new Blob([this._base64ToArrayBuffer(data)], {
      type: "application/doc"
    });

    var fileURL = URL.createObjectURL(blob);
    return fileURL;
}

_base64ToArrayBuffer(base64: string) {
  const binary_string = window.atob(base64);
  const len = binary_string.length;
  const bytes = new Uint8Array(len);
  for (let i = 0; i < len; i++) {
    bytes[i] = binary_string.charCodeAt(i);
  }
  return bytes.buffer;
}

}