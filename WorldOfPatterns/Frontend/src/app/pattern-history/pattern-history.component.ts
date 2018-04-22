import { Component, OnInit } from '@angular/core';
import {PatternService} from "../pattern.service";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {PatternRevision} from "../pattern-revision";

@Component({
  selector: 'app-pattern-history',
  templateUrl: './pattern-history.component.html',
  styleUrls: ['./pattern-history.component.css']
})
export class PatternHistoryComponent implements OnInit {
  pattern_name : string;
  revisions : PatternRevision[];

  constructor(
    private patternService: PatternService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit() {
    this.getPatternHistory();
  }

  goBack(): void {
    this.location.back();
  }

  getPatternHistory() : void {
    this.pattern_name = this.route.snapshot.paramMap.get('name');
    this.patternService.getPatternHistory(this.pattern_name)
      .subscribe(revisions => {
        this.revisions = revisions;
      });
  }

}
