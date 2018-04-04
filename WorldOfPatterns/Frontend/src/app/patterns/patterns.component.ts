import { Component, OnInit } from '@angular/core';

import { Pattern } from '../pattern';
import { PatternService } from "../pattern.service";

@Component({
  selector: 'app-patterns',
  templateUrl: './patterns.component.html',
  styleUrls: ['./patterns.component.css']
})
export class PatternsComponent implements OnInit {
  patterns : Pattern[];

  constructor(private patternService: PatternService) { }

  ngOnInit() {
    this.getPatterns();
  }

  getPatterns(): void {
    this.patternService.getPatterns().subscribe(patterns => this.patterns = patterns);
  }

}
