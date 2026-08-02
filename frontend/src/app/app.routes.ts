import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { TasksListComponent } from './tasks-list/tasks-list.component';
import { AddTaskFormComponent } from './add-task-form/add-task-form.component';
import { PageNotFound } from './pageNotFound/pageNotFound.component';
import { RouteGuardService } from './services/route-guard.service';
import { NoAuthGuardService } from './services/no-auth-guard.service';

export const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'login', component: LoginComponent },
    { path: 'sign-up', component: SignUpComponent, canActivate : [NoAuthGuardService] },
    { path: 'tasks', component: TasksListComponent, canActivate : [RouteGuardService] },
    { path: 'add-task-form', component: AddTaskFormComponent, canActivate : [RouteGuardService] },    
    { path: '**', component: PageNotFound, canActivate : [RouteGuardService]}
];
