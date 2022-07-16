import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import * as ace from 'ace-builds'; 
import 'ace-builds/src-noconflict/mode-javascript';
import 'ace-builds/src-noconflict/theme-github';
import 'ace-builds/src-noconflict/ext-language_tools';
import 'ace-builds/src-noconflict/ext-beautify';

const THEME = 'ace/theme/github'; 
const LANG = 'ace/mode/javascript';

@Component({
  selector: 'app-ide',
  templateUrl: './ide.component.html',
  styleUrls: ['./ide.component.css']
})


export class IdeComponent implements OnInit {

  @ViewChild('codeEditor')
  codeEditorElmRef: ElementRef | undefined;
  private codeEditor: ace.Ace.Editor | undefined;
  private editorBeautify: any;
  constructor() { }

  ngOnInit(): void {
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
  console.log(code);
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
}
