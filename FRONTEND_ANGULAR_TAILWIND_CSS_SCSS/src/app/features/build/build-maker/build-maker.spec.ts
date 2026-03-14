import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildMaker } from './build-maker';

describe('BuildMaker', () => {
  let component: BuildMaker;
  let fixture: ComponentFixture<BuildMaker>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BuildMaker],
    }).compileComponents();

    fixture = TestBed.createComponent(BuildMaker);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
