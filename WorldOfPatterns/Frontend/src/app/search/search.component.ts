import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {PatternService} from "../pattern.service";
import {Pattern} from "../pattern";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  patterns : Pattern[];

  error : string;
  loadingError = false;
  public loading = true;

  constructor(
    private patternService: PatternService,
    private route: ActivatedRoute,
  ) { }

  ngOnInit() {
    this.getSearchResults();
  }

  getSearchResults() {
    const query = this.route.snapshot.paramMap.get('query');
    this.patternService.searchPattern(query.toLowerCase())
      .subscribe(patterns => {
        this.patterns = patterns;
        this.loading = false;
        },
        error => {
          this.error = 'Error searching pattern!';
          console.error(error);
          this.loadingError = true;
        },);
  }

}
