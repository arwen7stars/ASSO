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

  error : string;
  loadingError = false;
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
      },
        error => {
          this.error = 'Error loading patterns!';
          console.error(error);
          this.loading = false;
          this.loadingError = true;
          },);
  }

  getLastModified() : void {
    let updated = 1;

    for(let i = 0; i < this.patterns.length; i++) {
      let revisions = new Array();

      this.patternService.getPatternHistory(this.patterns[i].id)
        .subscribe(function (i, result) {
          updated++;

          revisions = result;
          this.patterns[i].lastModified = this.patternService.getLastModified(revisions).toUTCString();
          this.patterns[i].lastMessage = revisions[0].message;

          if(updated == this.patterns.length) {

            this.patterns.sort((a, b) => {
              let date_a = Date.parse(a.lastModified);
              let date_b = Date.parse(b.lastModified);

              return date_a - date_b;
            });

            this.loading = false;
          }

          }.bind(this, i)),
          error => {
            this.error = 'Error loading last modified dates!';
            console.error(error);
            this.loading = false;
            this.loadingError = true;
          };
    }
  }
}
