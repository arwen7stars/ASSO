import { Component, OnInit, Input } from '@angular/core';
import {PatternService} from "../pattern.service";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {Pattern} from "../pattern";

@Component({
  selector: 'app-edit-pattern',
  templateUrl: './edit-pattern.component.html',
  styleUrls: ['./edit-pattern.component.css']
})
export class EditPatternComponent implements OnInit {
  @Input() pattern: Pattern;
  @Input() message: string;
  public loading = true;
  public updating = false;

  constructor(
    private patternService: PatternService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit() {
    this.getPattern();
    this.message = "";
  }

  getPattern(): void {
    this.loading = true;
    const id = +this.route.snapshot.paramMap.get('id');
    this.patternService.getPattern(id)
      .subscribe(pattern => {
        this.pattern = pattern;
        this.loading = false;
      });
  }

  submit(): void {
    this.updating = true;
    this.patternService.updatePattern(this.pattern.name, this.pattern.id, this.pattern.markdown, this.message)
      .subscribe(pattern => {
        this.pattern = pattern;
        this.location.back();
        this.updating = false;
      });
  }
}
