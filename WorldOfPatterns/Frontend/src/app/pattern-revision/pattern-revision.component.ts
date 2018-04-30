import { Component, OnInit } from '@angular/core';
import {PatternService} from "../pattern.service";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {Pattern} from "../pattern";

@Component({
  selector: 'app-pattern-revision',
  templateUrl: './pattern-revision.component.html',
  styleUrls: ['./pattern-revision.component.css']
})
export class PatternRevisionComponent implements OnInit {
  pattern: Pattern;
  public loading = true;

  constructor(
    private patternService: PatternService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit() {
    this.getPatternRevision();
  }

  getPatternRevision(): void {
    this.loading = true;
    let id = +this.route.snapshot.paramMap.get('id');
    let sha = this.route.snapshot.paramMap.get('sha');

    this.patternService.getPatternRevision(id, sha)
      .subscribe(pattern => {
        this.pattern = pattern;
        this.loading = false;
      });
  }

  goBack(): void {
    this.location.back();
  }

}
