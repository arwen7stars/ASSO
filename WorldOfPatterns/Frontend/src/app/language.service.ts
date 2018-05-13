import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Language} from "./language";
import {Observable} from "rxjs/Observable";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class LanguageService {
  private languageUrl = 'http://localhost:8080/languages';

  constructor(private http: HttpClient) { }

  /** GET languages from the server */
  getLanguages (): Observable<Language[]> {
    return this.http.get<Language[]>(this.languageUrl);
  }

  /** GET language by id */
  getLanguage(id: number): Observable<Language> {
    const url = `${this.languageUrl}/${id}`;
    return this.http.get<Language>(url);
  }

  /** Update language by id**/
  updateLanguage(id: number, name: string, ids: number[]) : Observable<any> {
    const url = `${this.languageUrl}/${id}`;

    let data = {
      name: name,
      ids: ids
    };

    return this.http.put(url, data, httpOptions);
  }

  /** Creates a new language */
  addLanguage(name: string, ids: number[]): Observable<Language> {
    const url = `${this.languageUrl}`;

    let data = {
      name: name,
      ids: ids
    };

    return this.http.post<Language>(url, data, httpOptions);
  }

}
