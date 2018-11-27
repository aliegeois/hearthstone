import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConstantesComponent } from './constantes.component';

describe('ConstantesComponent', () => {
  let component: ConstantesComponent;
  let fixture: ComponentFixture<ConstantesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConstantesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConstantesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
