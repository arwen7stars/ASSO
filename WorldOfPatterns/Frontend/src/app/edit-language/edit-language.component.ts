import {Component, Input, OnInit} from '@angular/core';
import {Language} from "../language";
import {Location} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {LanguageService} from "../language.service";
import {Pattern} from "../pattern";
import {PatternService} from "../pattern.service";

@Component({
  selector: 'app-edit-language',
  templateUrl: './edit-language.component.html',
  styleUrls: ['./edit-language.component.css']
})
export class EditLanguageComponent implements OnInit {
  @Input() language: Language;
  @Input() otherPatterns: Pattern[];

  error : string;
  loadingError = false;
  public loading = true;
  public updating = false;

  constructor(
    private languageService: LanguageService,
    private patternService: PatternService,
    private route: ActivatedRoute,
    private location: Location
  ) {
    this.otherPatterns = [];
  }

  ngOnInit() {
    this.getLanguage();
  }

  getLanguage(): void {
    this.loading = true;
    const id = +this.route.snapshot.paramMap.get('id');
    this.languageService.getLanguage(id)
      .subscribe(language => {
        this.language = language;
        this.getPatterns();
      },
        error => {
          this.error = 'Error loading language!';

          console.error(error);

          this.loading = false;
          this.loadingError = true;
        },);
  }

  getPatterns(): void {
    this.patternService.getPatterns()
      .subscribe(patterns => {

        patterns.forEach((pattern) => {
          let found = false;
          for(let i = 0; i < this.language.patterns.length; i++) {
            if (this.language.patterns[i].id == pattern.id) {
              found = true;
            }
          }
          if (!found){
            this.otherPatterns.push(pattern);
          }
        });

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
    this.language.patterns.push(pattern);
  }

  delete(pattern: Pattern): void {
    this.language.patterns = this.language.patterns.filter(p => p !== pattern);
    this.otherPatterns.push(pattern);
  }

  submit(): void {
    this.updating = true;
    let ids = [];
    this.language.patterns.forEach((pattern) => { ids.push(pattern.id) });

    this.languageService.updateLanguage(this.language.id, this.language.name, ids)
      .subscribe(language => {
        this.language = language;
        this.location.back();
        this.updating = false;
      },
        error => {
          this.error = 'Error updating language!';

          console.error(error);

          this.loading = false;
          this.loadingError = true;
        },);
  }

}
