import {Component, OnInit} from '@angular/core';
import {PatternService} from "../pattern.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-add-pattern',
  templateUrl: './add-pattern.component.html',
  styleUrls: ['./add-pattern.component.css']
})
export class AddPatternComponent implements OnInit {
  error : string;
  loadingError = false;
  public loading = false;

  constructor(
    private patternService: PatternService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {

  }

  add(name: string, markdown: string): void {
    this.loading = true;
    this.patternService.addPattern(name, markdown)
      .subscribe(() => {
        this.router.navigate(['/patterns']);
        this.loading = false;
      },
        error => {
          this.error = 'Error creating pattern!';

          console.error(error);

          this.loading = false;
          this.loadingError = true;
        },);
  }

}
