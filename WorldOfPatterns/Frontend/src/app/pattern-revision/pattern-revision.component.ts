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

  constructor(
    private patternService: PatternService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit() {
    this.getPatternRevision();
  }

  getPatternRevision(): void {
    var name = this.route.snapshot.paramMap.get('name');
    var sha = this.route.snapshot.paramMap.get('sha');

    this.patternService.getPatternRevision(name, sha)
      .subscribe(pattern => this.pattern = pattern);
  }

  goBack(): void {
    this.location.back();
  }

}
