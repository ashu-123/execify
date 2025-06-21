import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CodeExecutionService } from './service/code-execution.service';
import { CodemirrorModule } from '@ctrl/ngx-codemirror';

import 'codemirror/mode/javascript/javascript';
import 'codemirror/mode/python/python';
import 'codemirror/mode/clike/clike'; // for Java

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, CodemirrorModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  code = '';
  output = '';
  selectedLanguage = 'javascript';

  constructor(private codeExecutionService: CodeExecutionService) { }

  languages = [
    { label: 'JavaScript', value: 'javascript' },
    { label: 'Python', value: 'python' },
    { label: 'Java', value: 'text/x-java' }, // codemirror mode for java
  ];

  editorOptions = {
    lineNumbers: true,
    mode: this.selectedLanguage,
    theme: 'material',
  };

  onLanguageChange() {
    this.editorOptions = {
      ...this.editorOptions,
      mode: this.selectedLanguage,
    };
  }

  runCode() {
    this.codeExecutionService.executeCode({ code: this.code, language: this.selectedLanguage })
      .subscribe({
        next: (res) => this.output = res.output
      });
  }
}
