import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PatternHistoryComponent } from './pattern-history.component';

describe('PatternHistoryComponent', () => {
  let component: PatternHistoryComponent;
  let fixture: ComponentFixture<PatternHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PatternHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PatternHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
