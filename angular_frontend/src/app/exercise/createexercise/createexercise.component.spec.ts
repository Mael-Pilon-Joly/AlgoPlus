import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateexerciseComponent } from './createexercise.component';

describe('CreateexerciseComponent', () => {
  let component: CreateexerciseComponent;
  let fixture: ComponentFixture<CreateexerciseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateexerciseComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateexerciseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
