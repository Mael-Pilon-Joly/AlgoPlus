import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordrequestComponent } from './passwordrequest.component';

describe('PasswordrequestComponent', () => {
  let component: PasswordrequestComponent;
  let fixture: ComponentFixture<PasswordrequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PasswordrequestComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordrequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
