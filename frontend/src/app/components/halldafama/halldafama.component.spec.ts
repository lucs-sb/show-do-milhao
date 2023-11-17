import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HalldafamaComponent } from './halldafama.component';

describe('HalldafamaComponent', () => {
  let component: HalldafamaComponent;
  let fixture: ComponentFixture<HalldafamaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HalldafamaComponent]
    });
    fixture = TestBed.createComponent(HalldafamaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
