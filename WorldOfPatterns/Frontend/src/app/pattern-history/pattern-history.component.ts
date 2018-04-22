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
  pattern_id : number;
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
    this.pattern_id = +this.route.snapshot.paramMap.get('id');
    this.patternService.getPatternHistory(this.pattern_id)
      .subscribe(revisions => {
        this.revisions = revisions;
      });
  }

}
