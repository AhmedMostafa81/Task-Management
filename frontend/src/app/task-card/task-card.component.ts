import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { ApiService } from '../services/api.service';
import { SweetAlertService } from '../services/alet/sweet-alert.service';


@Component({
  selector: 'app-task-card',
  standalone: true,
  imports: [],
  templateUrl: './task-card.component.html',
  styleUrl: './task-card.component.css'
})
export class TaskCardComponent {

  @Input() task!: {
    id: number;
    title: string;
    description: string;
    status: string;
    priority?: string;
  };

  @Output() taskDeleted = new EventEmitter<any>();

  constructor(private router: Router, private auth: AuthenticationService, private api: ApiService,private alert:SweetAlertService) { }


  onClickView(taskId: any) {
    this.router.navigate(['add-task-form'], { state: { from: 'view', taskId: taskId } });
  }
  onClickDelete(taskId: any,taskTitle:any) {
    this.api.deleteTaskById(taskId).subscribe(
      response => {
        console.log(response);
        this.taskDeleted.emit(this.task.title);
        this.alert.Toast.fire({
          icon: "success",
          title: `Task: ${taskTitle} has been removed.`
        });
      },
      error => {
        console.log(error);
      }
    )
  }

  getPriorityClass(priority: string | undefined): string {
    return (priority || 'LOW').toUpperCase();
  }

}
