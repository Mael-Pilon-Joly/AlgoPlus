import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmationsuccessComponent } from './confirmationsuccess.component';

describe('ConfirmationsuccessComponent', () => {
  let component: ConfirmationsuccessComponent;
  let fixture: ComponentFixture<ConfirmationsuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmationsuccessComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmationsuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
