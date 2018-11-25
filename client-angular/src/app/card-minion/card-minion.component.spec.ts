import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardMinionComponent } from './card-minion.component';

describe('CardMinionComponent', () => {
  let component: CardMinionComponent;
  let fixture: ComponentFixture<CardMinionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardMinionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardMinionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
