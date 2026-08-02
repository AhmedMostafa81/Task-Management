import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { SweetAlertService } from '../services/alet/sweet-alert.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, NgIf, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username = '';
  password = '';
  invalidLogin = false;

  constructor(private route: Router, private auth: AuthenticationService,private alert:SweetAlertService) { }

  onLogin() {
    this.auth.authenticate(this.username, this.password).subscribe(
      data => {
        console.log("Login successful!");
        this.route.navigate(['tasks'])
        this.alert.Toast.fire({
          icon: "success",
          title: "You have logged in successfully."
        });
      },
      error => {
        console.error("Login failed", error);
        this.invalidLogin = true;
      }
    );
  }
}
