import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PatternsComponent } from "./patterns/patterns.component";
import { PatternDetailComponent } from "./pattern-detail/pattern-detail.component";
import { AddPatternComponent } from "./add-pattern/add-pattern.component";

const routes: Routes = [
  { path: '', redirectTo: '/patterns', pathMatch: 'full' },
  { path: 'patterns', component: PatternsComponent },
  { path: 'pattern/:id', component: PatternDetailComponent },
  { path: 'addPattern', component: AddPatternComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
