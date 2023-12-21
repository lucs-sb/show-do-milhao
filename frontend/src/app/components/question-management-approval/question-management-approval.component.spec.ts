import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionManagementApprovalComponent } from './question-management-approval.component';

describe('QuestionManagementApprovalComponent', () => {
  let component: QuestionManagementApprovalComponent;
  let fixture: ComponentFixture<QuestionManagementApprovalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestionManagementApprovalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(QuestionManagementApprovalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
