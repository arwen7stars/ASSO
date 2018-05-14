import {Component, Input, OnInit} from '@angular/core';
import {Language} from "../language";
import {Pattern} from "../pattern";
import {PatternService} from "../pattern.service";
import {ActivatedRoute, Router} from "@angular/router";
import {LanguageService} from "../language.service";

@Component({
  selector: 'app-add-language',
  templateUrl: './add-language.component.html',
  styleUrls: ['./add-language.component.css']
})
export class AddLanguageComponent implements OnInit {
  public loading = true;
  public updating = false;

  error : string;
  loadingError = false;
  @Input() patterns: Pattern[];
  @Input() otherPatterns: Pattern[];

  constructor(
    private patternService: PatternService,
    private languageService: LanguageService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.patterns = [];
    this.getPatterns();
  }

  getPatterns(): void {
    this.loading = true;
    this.patternService.getPatterns()
      .subscribe(patterns => {
        this.otherPatterns = patterns;
        this.loading = false;
      },
        error => {
          this.error = 'Error loading patterns!';

          console.error(error);

          this.loading = false;
          this.loadingError = true;
        },);
  }

  add(pattern: Pattern): void {
    this.otherPatterns = this.otherPatterns.filter(p => p !== pattern);
    this.patterns.push(pattern);
  }

  delete(pattern: Pattern): void {
    this.patterns = this.patterns.filter(p => p !== pattern);
    this.otherPatterns.push(pattern);
  }

  submit(name: string): void {
    this.updating = true;
    var ids = [];
    this.patterns.forEach((pattern) => { ids.push(pattern.id) });

    this.languageService.addLanguage(name, ids)
      .subscribe(() => {
        this.router.navigate(['/languages']);
        this.updating = false;
      },
        error => {
          this.error = 'Error creating language!';

          console.error(error);

          this.loading = false;
          this.loadingError = true;
        },);
  }
}
