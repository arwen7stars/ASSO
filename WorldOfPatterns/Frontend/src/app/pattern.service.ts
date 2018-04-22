import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError } from 'rxjs/operators';

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

  /** GET pattern by name */
  getPattern(name: string): Observable<Pattern> {
    const url = `${this.patternUrl}/${name}`;
    return this.http.get<Pattern>(url);
  }

  updatePattern(name: string, markdown: string, message: string) : Observable<any> {
    const url = `${this.patternUrl}/${name}`;

    if(!message) {
      message = 'Updated ' + name;
    }

    let data = {
      markdown: markdown,
      message: message
    };

    return this.http.post(url, data, httpOptions);
  }

  getPatternHistory(name: string): Observable<PatternRevision[]> {
    const url = `${this.patternUrl}/${name}/history`;
    return this.http.get<PatternRevision[]>(url);
  }

  getLastModified(revisions: PatternRevision[]) : Date {
    var revision = revisions[0];            // the first element has the latest date

    return new Date(Date.parse(revision.date));
  }

  timeSince(date : Date): string {
    var elapsedTime = Date.now() - date.getTime();

    var seconds = Math.floor(elapsedTime / 1000);
    var interval = Math.floor(seconds / 31536000);

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

  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
