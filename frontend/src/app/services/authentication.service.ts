import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { tap } from 'rxjs';

export const TOKEN = 'token';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private api: ApiService) { }
  
  authenticate(username: string, password: string) {
    // Call the new login method in ApiService
    return this.api.login(username, password).pipe(
      tap((data: any) => {
        console.log("Login successful, response:", data);
        
        // Assuming your backend AuthResponseDTO has a property called 'token'.
        // If it's named something else (like 'accessToken'), change data.token accordingly.
        const jwtToken = data.token;
        
        // Store the JWT token with the 'Bearer ' prefix
        sessionStorage.setItem(TOKEN, 'Bearer ' + jwtToken);
      })
    );
  }
  
  logout(): void {
    sessionStorage.removeItem(TOKEN);
  } 

  isUserLoggedIn() {
    return sessionStorage.getItem(TOKEN) !== null;
  }

  getToken() {
    return sessionStorage.getItem(TOKEN) || '';
  }
}