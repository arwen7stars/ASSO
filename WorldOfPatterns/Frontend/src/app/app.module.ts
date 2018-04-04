import { NgModule } from '@angular/core';
import { AlertModule } from 'ngx-bootstrap';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms'; // <-- NgModel lives here

import { PatternService }          from './pattern.service';

import { AppComponent } from './app.component';
import { PatternSearchComponent } from './pattern-search/pattern-search.component';
import { NavbarComponent } from './navbar/navbar.component';
import { PatternsComponent } from './patterns/patterns.component';
import { AppRoutingModule } from './/app-routing.module';
import { PatternDetailComponent } from './pattern-detail/pattern-detail.component';
import { AddPatternComponent } from './add-pattern/add-pattern.component';


@NgModule({
  declarations: [
    AppComponent,
    PatternSearchComponent,
    NavbarComponent,
    PatternsComponent,
    PatternDetailComponent,
    AddPatternComponent
  ],
  imports: [
    AlertModule.forRoot(),
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [PatternService],
  bootstrap: [AppComponent]
})
export class AppModule { }
