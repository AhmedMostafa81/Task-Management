import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  // Updated to include /api prefix
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  // ==========================================
  // AUTHENTICATION ENDPOINTS
  // ==========================================

  signup(username: string, password: string) {
    const userCredentials = { "username": username, "password": password };
    return this.http.post(`${this.baseUrl}/auth/register`, userCredentials);
  }

  // Replaces the old basic auth getUserId()
  login(username: string, password: string) {
    const userCredentials = { "username": username, "password": password };
    return this.http.post(`${this.baseUrl}/auth/login`, userCredentials);
  }

  // ==========================================
  // TASK ENDPOINTS (No userId needed in params)
  // ==========================================

  getTasks(params: any = {}) {
    let httpParams = new HttpParams({ fromObject: params });
    // Hits: GET /api/tasks?status=...&priority=...
    return this.http.get(`${this.baseUrl}/tasks`, { params: httpParams });
  }

  getTask(taskId: string | number) {
    // Hits: GET /api/tasks/{id}
    return this.http.get(`${this.baseUrl}/tasks/${taskId}`);
  }

  createTask(task: any) {
    const bodyRequest = {
      "title": task.title,
      "description": task.description,
      "status": task.status,
      "priority": task.priority
    };
    // Hits: POST /api/tasks
    return this.http.post(`${this.baseUrl}/tasks`, bodyRequest);
  }

  updateTask(taskId: string | number, task: any) {
    const bodyRequest = {
      "title": task.title,
      "description": task.description,
      "status": task.status,
      "priority": task.priority
    };
    // Hits: PUT /api/tasks/{id}
    return this.http.put(`${this.baseUrl}/tasks/${taskId}`, bodyRequest);
  }

  deleteTaskById(taskId: string | number) {
    // Hits: DELETE /api/tasks/{id}
    return this.http.delete(`${this.baseUrl}/tasks/${taskId}`);
  }
}