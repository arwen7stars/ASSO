import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PatternsComponent } from "./patterns/patterns.component";
import { PatternDetailComponent } from "./pattern-detail/pattern-detail.component";
import { AddPatternComponent } from "./add-pattern/add-pattern.component";
import {PatternHistoryComponent} from "./pattern-history/pattern-history.component";
import {EditPatternComponent} from "./edit-pattern/edit-pattern.component";
import {PatternRevisionComponent} from "./pattern-revision/pattern-revision.component";
import {LanguagesComponent} from "./languages/languages.component";
import {LanguageDetailComponent} from "./language-detail/language-detail.component";
import {AddLanguageComponent} from "./add-language/add-language.component";
import {EditLanguageComponent} from "./edit-language/edit-language.component";

const routes: Routes = [
  { path: '', redirectTo: '/patterns', pathMatch: 'full' },
  { path: 'patterns', component: PatternsComponent },
  { path: 'pattern/:id', component: PatternDetailComponent },
  { path: 'pattern/:id/edit', component: EditPatternComponent },
  { path: 'pattern/:id/history', component: PatternHistoryComponent },
  { path: 'pattern/:id/history/:sha', component: PatternRevisionComponent },
  { path: 'addPattern', component: AddPatternComponent },
  { path: 'languages', component: LanguagesComponent },
  { path: 'language/:id', component: LanguageDetailComponent },
  { path: 'language/:id/edit', component: EditLanguageComponent },
  { path: 'addLanguage', component: AddLanguageComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
