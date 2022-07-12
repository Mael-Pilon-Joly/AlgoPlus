import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WritearticleComponent } from './writearticle.component';

describe('WritearticleComponent', () => {
  let component: WritearticleComponent;
  let fixture: ComponentFixture<WritearticleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WritearticleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WritearticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
