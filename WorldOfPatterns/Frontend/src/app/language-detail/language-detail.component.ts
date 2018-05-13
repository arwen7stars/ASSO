import { Component, OnInit } from '@angular/core';
import {Location} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {LanguageService} from "../language.service";
import {Language} from "../language";

@Component({
  selector: 'app-language-detail',
  templateUrl: './language-detail.component.html',
  styleUrls: ['./language-detail.component.css']
})
export class LanguageDetailComponent implements OnInit {
  public loading = true;
  language: Language;

  constructor(
    private languageService: LanguageService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit() {
    this.getLanguage()
  }

  goBack(): void {
    this.location.back();
  }

  getLanguage(): void {
    this.loading = true;
    const id = +this.route.snapshot.paramMap.get('id');
    this.languageService.getLanguage(id).subscribe(language => {
      this.language = language;
      console.log(this.language.patterns);
      this.loading = false;
    });
  }

}
