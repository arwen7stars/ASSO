import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Pattern } from '../pattern';
import { PatternService } from "../pattern.service";

@Component({
  selector: 'app-pattern-detail',
  templateUrl: './pattern-detail.component.html',
  styleUrls: ['./pattern-detail.component.css']
})
export class PatternDetailComponent implements OnInit {
  @Input() pattern: Pattern;

  constructor(
    private patternService: PatternService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit() {
    this.getHero()
  }

  goBack(): void {
    this.location.back();
  }

  getHero(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.patternService.getPattern(id).subscribe(pattern => this.pattern = pattern);
  }

}
