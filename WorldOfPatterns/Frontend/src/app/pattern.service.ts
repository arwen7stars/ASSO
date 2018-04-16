import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';

import { Pattern } from './pattern';

@Injectable()
export class PatternService {
  private patternUrl = 'http://localhost:8080/patterns';

  constructor(private http: HttpClient) { }

  /** GET patterns from the server */
  getPatterns (): Observable<Pattern[]> {
    return this.http.get<Pattern[]>(this.patternUrl);
  }

  /** GET hero by id. Will 404 if id not found */
  getPattern(id: number): Observable<Pattern> {
    const url = `${this.patternUrl}/${id}`;
    return this.http.get<Pattern>(url);
  }


}
