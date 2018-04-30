import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Pattern } from '../pattern';
import { PatternRevision } from "../pattern-revision";
import { PatternService } from "../pattern.service";

@Component({
  selector: 'app-pattern-detail',
  templateUrl: './pattern-detail.component.html',
  styleUrls: ['./pattern-detail.component.css']
})
export class PatternDetailComponent implements OnInit {
  pattern: Pattern;
  revisions: PatternRevision[];
  public loading = true;

  constructor(
    private patternService: PatternService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit() {
    this.getPattern()
  }

  goBack(): void {
    this.location.back();
  }

  getPattern(): void {
    this.loading = true;
    const id = +this.route.snapshot.paramMap.get('id');
    this.patternService.getPattern(id).subscribe(pattern => {
      this.pattern = pattern;
      this.getRevisionHistory();
    });
  }

  getRevisionHistory(): void {
    this.patternService.getPatternHistory(this.pattern.id)
      .subscribe(revisions => {
        this.revisions = revisions;
        var date = this.patternService.getLastModified(revisions);

        this.pattern.lastModified = this.patternService.timeSince(date);
        this.pattern.lastMessage = this.revisions[0].message;
        this.loading = false;
      });
  }




}
