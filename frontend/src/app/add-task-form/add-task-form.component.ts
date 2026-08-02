import { Component } from '@angular/core';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { ApiService } from '../services/api.service';
import { NgIf } from '@angular/common';
import { SweetAlertService } from '../services/alet/sweet-alert.service';

@Component({
  selector: 'app-add-task-form',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './add-task-form.component.html',
  styleUrl: './add-task-form.component.css'
})
export class AddTaskFormComponent {
  from: string | null = null;
  taskId: string = '';
  action: string = '';
  header: string = '';

  task: FormGroup;


  constructor(private router: Router, private auth: AuthenticationService, private api: ApiService,private alert:SweetAlertService) {
    this.task = new FormGroup({
      title: new FormControl(''),
      description: new FormControl(''),
      status: new FormControl('TODO'),
      priority: new FormControl('MEDIUM')
    });
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras.state as { from?: string, taskId?: any };
    this.from = state?.from || null;
    this.taskId = state?.taskId || null;
    if (this.from === 'view') {
      this.header = 'View'
      this.action = 'Save';
    }
    else {
      this.header = 'Add';
      this.action = 'Add';
    }
  }

  ngOnInit() {
    this.api.getTask(this.taskId).subscribe(
      (response: any) => {
        console.log(response); // response contains task info

        this.task.patchValue({
          title: response.title,
          description: response.description,
          status: response.status,
          priority: response.priority || 'MEDIUM'
        });
      },
      (error: any) => {
        console.log(error.error.message);
      }
    )
  }

  errorMessage = '';

  onClickAdd() {
    if (this.action === 'Add') {
      this.api.createTask(this.task.value).subscribe(
        (response:any) => {
          console.log("Task created:", response);
          this.router.navigate(['tasks']);
          this.alert.Toast.fire({
            icon: "success",
            title: `Task: ${response.title} has been created.`
          });

        },
        (error:any) => {
          this.errorMessage = error?.error?.message || 'An error occurred while creating the task.';
          console.log(error?.error?.message);
        }
      );
    }
    else {
      this.api.updateTask(this.taskId, this.task.value).subscribe(
        (response:any) => {
          console.log("Task updated:", response);
          this.router.navigate(['tasks']);
          this.alert.Toast.fire({
            icon: "success",
            title: `Task: ${response.title} has been updated.`
          });
        },
        (error:any) => {
          this.errorMessage = error?.error?.message || 'An error occurred while updating the task.';
          console.log(error?.error?.message);
        }
      );
    }
  }


}
