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
    const name = this.route.snapshot.paramMap.get('name');
    this.patternService.getPattern(name)
      .subscribe(pattern => {
        this.pattern = pattern;
      });
  }

  submit(): void {
    this.patternService.updatePattern(this.pattern.name, this.pattern.markdown, this.message)
      .subscribe(pattern => {
        this.pattern = pattern;
        this.location.back();
      });
  }
}
