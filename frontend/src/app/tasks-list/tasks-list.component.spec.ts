import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TasksListComponent } from './tasks-list.component';

describe('TasksListComponent', () => {
  let component: TasksListComponent;
  let fixture: ComponentFixture<TasksListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TasksListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TasksListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should request tasks filtered by status and priority', () => {
    component.statusFilter = 'TODO';
    component.priorityFilter = 'HIGH';

    const loadTasksSpy = spyOn(component, 'loadTasks');

    component.onFilterTasks();

    expect(loadTasksSpy).toHaveBeenCalledWith({ status: 'TODO', priority: 'HIGH' });
  });
});
