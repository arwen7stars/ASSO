import {Component, OnInit} from '@angular/core';
import {PatternService} from "../pattern.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-add-pattern',
  templateUrl: './add-pattern.component.html',
  styleUrls: ['./add-pattern.component.css']
})
export class AddPatternComponent implements OnInit {
  constructor(
    private patternService: PatternService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {

  }

  add(name: string, markdown: string): void {

    this.patternService.addPattern(name, markdown)
      .subscribe(() => {
        this.router.navigate(['/patterns']);
      });
  }

}
