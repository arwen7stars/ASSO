import { Component, OnInit } from '@angular/core';

import {Language} from "../language";
import {LanguageService} from "../language.service";

@Component({
  selector: 'app-languages',
  templateUrl: './languages.component.html',
  styleUrls: ['./languages.component.css']
})
export class LanguagesComponent implements OnInit {
  languages : Language[];

  error : string;
  loadingError = false;
  public loading = true;

  constructor(private languageService: LanguageService) { }

  ngOnInit() {
    this.getLanguages();
  }

  getLanguages(): void {
    this.loading = true;
    this.languageService.getLanguages().subscribe(languages => {
      this.languages = languages;
      this.languages.sort((a, b) => {
        if (a.name < b.name) return -1;
        else if (a.name > b.name) return 1;
        else return 0;
      });
      this.loading = false;
    },
      error => {
        this.error = 'Error loading languages!';
        console.error(error);
        this.loading = false;
        this.loadingError = true;
      },);
  }
}
