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
  public loading = true;

  constructor(
    private patternService: PatternService,
    private app: AppComponent
  ) {}

  ngOnInit() {
    this.title = this.app.getTitle();
    this.getPatterns();
  }

  getPatterns(): void {
    this.loading = true;
    this.patternService.getPatterns()
      .subscribe(patterns => {
        this.patterns = patterns;
        this.getLastModified();
      });
  }

  getLastModified() : void {
    var updated = 1;

    for(var i = 0; i < this.patterns.length; i++) {
      var revisions = new Array();

      this.patternService.getPatternHistory(this.patterns[i].id)
        .subscribe(function (i, result) {
          updated++;

          revisions = result;
          this.patterns[i].lastModified = this.patternService.getLastModified(revisions).toUTCString();
          this.patterns[i].lastMessage = revisions[0].message;

          if(updated == this.patterns.length) {

            this.patterns.sort((a, b) => {
              var date_a = Date.parse(a.lastModified);
              var date_b = Date.parse(b.lastModified);

              return date_a - date_b;
            });

            this.loading = false;
          }
        }.bind(this, i));
    }
  }
}
