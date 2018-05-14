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

  error : string;
  loadingError = false;
  public loading = true;

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
    this.loading = true;
    this.pattern_id = +this.route.snapshot.paramMap.get('id');
    this.patternService.getPatternHistory(this.pattern_id)
      .subscribe(revisions => {
        this.revisions = revisions;
        this.loading = false;
      },
        error => {
          this.error = 'Error loading pattern history!';

          console.error(error);

          this.loading = false;
          this.loadingError = true;
        },);
  }

}
