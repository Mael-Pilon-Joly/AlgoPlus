import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReadarticleComponent } from './readarticle.component';

describe('ReadarticleComponent', () => {
  let component: ReadarticleComponent;
  let fixture: ComponentFixture<ReadarticleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReadarticleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReadarticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
