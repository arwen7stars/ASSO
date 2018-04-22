import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PatternsComponent } from "./patterns/patterns.component";
import { PatternDetailComponent } from "./pattern-detail/pattern-detail.component";
import { AddPatternComponent } from "./add-pattern/add-pattern.component";
import {PatternHistoryComponent} from "./pattern-history/pattern-history.component";
import {EditPatternComponent} from "./edit-pattern/edit-pattern.component";

const routes: Routes = [
  { path: '', redirectTo: '/patterns', pathMatch: 'full' },
  { path: 'patterns', component: PatternsComponent },
  { path: 'pattern/:name', component: PatternDetailComponent },
  { path: 'pattern/:name/edit', component: EditPatternComponent },
  { path: 'pattern/:name/history', component: PatternHistoryComponent },
  { path: 'addPattern', component: AddPatternComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
