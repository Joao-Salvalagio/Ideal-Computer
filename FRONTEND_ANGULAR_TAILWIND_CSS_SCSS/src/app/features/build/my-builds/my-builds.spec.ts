import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyBuilds } from './my-builds';

describe('MyBuilds', () => {
  let component: MyBuilds;
  let fixture: ComponentFixture<MyBuilds>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyBuilds],
    }).compileComponents();

    fixture = TestBed.createComponent(MyBuilds);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
