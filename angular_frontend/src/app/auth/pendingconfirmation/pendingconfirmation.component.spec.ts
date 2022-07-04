import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PendingconfirmationComponent } from './pendingconfirmation.component';

describe('PendingconfirmationComponent', () => {
  let component: PendingconfirmationComponent;
  let fixture: ComponentFixture<PendingconfirmationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PendingconfirmationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PendingconfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
