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

  error : string;
  loadingError = false;
  public loading = true;
  public updating = false;

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
      },
        error => {
          this.error = 'Error loading pattern revision!';

          console.error(error);

          this.loading = false;
          this.loadingError = true;
        },);
  }

  revert(): void {
    let sha = this.route.snapshot.paramMap.get('sha');
    let message = "Reverted back to " + sha;
    this.patternService.updatePattern(this.pattern.name, this.pattern.id, this.pattern.markdown, message)
      .subscribe(() => {
        this.location.back();
        },
        error => {
          this.error = 'Error updating pattern!';

          console.error(error);

          this.updating = false;
          this.loadingError = true;
          },);
  }

  goBack(): void {
    this.location.back();
  }

}
