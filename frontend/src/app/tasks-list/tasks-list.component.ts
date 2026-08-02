import { Component } from '@angular/core';
import { ApiService } from '../services/api.service';
import { TaskCardComponent } from '../task-card/task-card.component';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { AuthenticationService } from '../services/authentication.service';
import { FormsModule } from '@angular/forms';



@Component({
  selector: 'app-tasks-list',
  standalone: true,
  imports: [TaskCardComponent, NgIf, FormsModule],
  templateUrl: './tasks-list.component.html',
  styleUrl: './tasks-list.component.css'
})
export class TasksListComponent {
  tasks: any;
  statusFilter = '';
  priorityFilter = '';

  constructor(private api: ApiService, private auth: AuthenticationService, private router: Router) { }


  ngOnInit() {
    this.loadTasks();
  }

  loadTasks(params: any = {}) {
    this.api.getTasks(params).subscribe(
      (response: any) => {
        console.log("tasks loaded");
        console.log(response);
        this.tasks = response;
      },
      (error: any) => {
        console.log(error.error);
      }
    );
  }

  onTaskDeleted(taskTitle: string) {
    this.loadTasks();
  }

  onClickAddTask() {
    this.router.navigate(['add-task-form'], { state: { from: 'addTask' } });
  }
  onFilterTasks() {
    const params: any = {};

    if (this.statusFilter) {
      params.status = this.statusFilter;
    }

    if (this.priorityFilter) {
      params.priority = this.priorityFilter;
    }

    this.loadTasks(params);
  }

}



