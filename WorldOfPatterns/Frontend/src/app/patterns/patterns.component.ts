import { Component, OnInit } from '@angular/core';

import { Pattern } from '../pattern';
import { PatternService } from "../pattern.service";
import {AppComponent} from "../app.component";

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
    this.patternService.getPatterns().subscribe(patterns => this.patterns = patterns);
  }

}
