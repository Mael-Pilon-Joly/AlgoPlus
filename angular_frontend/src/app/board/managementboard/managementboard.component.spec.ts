import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagementboardComponent } from './managementboard.component';

describe('ManagementboardComponent', () => {
  let component: ManagementboardComponent;
  let fixture: ComponentFixture<ManagementboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManagementboardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagementboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
