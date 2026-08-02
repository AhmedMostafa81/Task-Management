import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskCardComponent } from './task-card.component';

describe('TaskCardComponent', () => {
  let component: TaskCardComponent;
  let fixture: ComponentFixture<TaskCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaskCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not display a due date label', () => {
    component.task = {
      id: 1,
      title: 'Test task',
      description: 'Task description',
      status: 'TODO',
      priority: 'HIGH'
    };

    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).not.toContain('Due:');
  });
});
