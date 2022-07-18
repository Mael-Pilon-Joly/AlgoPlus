import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeexerciseComponent } from './homeexercise.component';

describe('HomeexerciseComponent', () => {
  let component: HomeexerciseComponent;
  let fixture: ComponentFixture<HomeexerciseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomeexerciseComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeexerciseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
