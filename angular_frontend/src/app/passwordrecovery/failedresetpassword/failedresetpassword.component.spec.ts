import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FailedresetpasswordComponent } from './failedresetpassword.component';

describe('FailedresetpasswordComponent', () => {
  let component: FailedresetpasswordComponent;
  let fixture: ComponentFixture<FailedresetpasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FailedresetpasswordComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FailedresetpasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
