import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvalidtokenComponent } from './invalidtoken.component';

describe('InvalidtokenComponent', () => {
  let component: InvalidtokenComponent;
  let fixture: ComponentFixture<InvalidtokenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InvalidtokenComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InvalidtokenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
