import { Component, OnInit } from '@angular/core';
import { AppComponent } from "../app.component"
import {PatternService} from "../pattern.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  title : string;

  constructor(
    private app : AppComponent,
    private router: Router
  ) {}

  ngOnInit() {
    this.title = this.app.getTitle();
  }

  search(query: string) : void {
    if(query != "") {
      this.router.navigate(['/search/', query.toLowerCase()]);
    }
  }

}
