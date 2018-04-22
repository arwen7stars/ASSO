import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PatternRevisionComponent } from './pattern-revision.component';

describe('PatternRevisionComponent', () => {
  let component: PatternRevisionComponent;
  let fixture: ComponentFixture<PatternRevisionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PatternRevisionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PatternRevisionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
