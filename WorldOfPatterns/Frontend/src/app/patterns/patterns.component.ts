import { Component, OnInit } from '@angular/core';

import { AppComponent } from "../app.component";
import { Pattern } from '../pattern';
import { PatternService } from "../pattern.service";

@Component({
  selector: 'app-patterns',
  templateUrl: './patterns.component.html',
  styleUrls: ['./patterns.component.css']
})
export class PatternsComponent implements OnInit {
  title : string;
  patterns : Pattern[];

  constructor(
    private patternService: PatternService,
    private app: AppComponent
  ) {}

  ngOnInit() {
    this.title = this.app.getTitle();
    this.getPatterns();
  }

  getPatterns(): void {
    this.patternService.getPatterns()
      .subscribe(patterns => {
        this.patterns = patterns;
        this.getLastModified();
      });
  }

  getLastModified() : void {
    for(var i = 0; i < this.patterns.length; i++) {
      var revisions = new Array();

      this.patternService.getPatternHistory(this.patterns[i].name)
        .subscribe(function (i, result) {
          revisions = result;
          this.patterns[i].lastModified = this.patternService.getLastModified(revisions).toUTCString();
          this.patterns[i].lastMessage = revisions[0].message;
        }.bind(this, i));
    }
  }
}
