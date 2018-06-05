import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';

import { Pattern } from './pattern';
import { PatternRevision } from "./pattern-revision";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class PatternService {
  private patternUrl = 'http://localhost:8080/patterns';

  constructor(private http: HttpClient) { }

  /** GET patterns from the server */
  getPatterns (): Observable<Pattern[]> {
    return this.http.get<Pattern[]>(this.patternUrl);
  }

  /** GET pattern by id */
  getPattern(id: number): Observable<Pattern> {
    const url = `${this.patternUrl}/${id}`;
    return this.http.get<Pattern>(url);
  }

  /** Update pattern by id**/
  updatePattern(name: string, id: number, markdown: string, message: string) : Observable<any> {
    const url = `${this.patternUrl}/${id}`;

    if(!message) {
      message = 'Updated ' + name;
    }

    let data = {
      markdown: markdown,
      message: message
    };

    return this.http.put(url, data, httpOptions);
  }

  /** Creates a new pattern */
  addPattern(name: string, markdown: string): Observable<Pattern> {
    const url = `${this.patternUrl}`;

    let data = {
      name: name,
      markdown: markdown,
    };

    return this.http.post<Pattern>(url, data, httpOptions);
  }

  /** Gets an array of pattern revisions given its id*/
  getPatternHistory(id: number): Observable<PatternRevision[]> {
    const url = `${this.patternUrl}/${id}/history`;
    return this.http.get<PatternRevision[]>(url);
  }

  /** Gets a single pattern revision*/
  getPatternRevision(id: number, sha: string): Observable<Pattern> {
    const url = `${this.patternUrl}/${id}/history/${sha}`;
    return this.http.get<Pattern>(url);
  }

  searchPattern(query: string) : Observable<Pattern[]> {
    const url = `${this.patternUrl}/search/${query}`;
    return this.http.get<Pattern[]>(url);
  }

  getLastModified(revisions: PatternRevision[]) : Date {
    let revision = revisions[0];            // the first element has the latest date

    return new Date(Date.parse(revision.date));
  }

  timeSince(date : Date): string {
    let elapsedTime = Date.now() - date.getTime();

    let seconds = Math.floor(elapsedTime / 1000);
    let interval = Math.floor(seconds / 31536000);

    if (interval > 1) {
      return interval + " years";
    }
    interval = Math.floor(seconds / 2592000);
    if (interval > 1) {
      return interval + " months";
    }
    interval = Math.floor(seconds / 86400);
    if (interval > 1) {
      return interval + " days";
    }
    interval = Math.floor(seconds / 3600);
    if (interval > 1) {
      return interval + " hours";
    }
    interval = Math.floor(seconds / 60);
    if (interval > 1) {
      return interval + " minutes";
    }
    return Math.floor(seconds) + " seconds";
  }
}
