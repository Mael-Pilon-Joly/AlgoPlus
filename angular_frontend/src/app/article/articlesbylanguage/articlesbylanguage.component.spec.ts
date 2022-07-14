import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticlesbylanguageComponent } from './articlesbylanguage.component';

describe('ArticlesbylanguageComponent', () => {
  let component: ArticlesbylanguageComponent;
  let fixture: ComponentFixture<ArticlesbylanguageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArticlesbylanguageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArticlesbylanguageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
