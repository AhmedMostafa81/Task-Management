import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../services/api.service';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { SweetAlertService } from '../services/alet/sweet-alert.service';


@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [RouterLink, FormsModule, NgIf],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css'
})
export class SignUpComponent {

  username = '';
  password = '';
  confirmPassword = '';
  
  showWarningMessage = false;
  warningMessage = '';
  

  constructor(private api: ApiService,private router:Router,private alert:SweetAlertService) { }

  onSignup() {
    let validToSignup: boolean = (
    this.password === this.confirmPassword
    && this.password.trim().length > 0
    && this.username.trim().length > 0
    )
    if (validToSignup) {
      this.api.signup(this.username, this.password).subscribe(
        response => {
          console.log(response);
          this.router.navigate(['login'])
          this.alert.Toast.fire({
            icon: "success",
            title: "Signup successful! You can now log in."
          });
        },
        error => {
          this.showWarningMessage = true;
          this.warningMessage = error?.error?.message || 'An error occurred during signup.';
          console.log( error?.error?.message);
        }
      )
    }
    else {
      this.warningMessage = 'Invalid inputs';
      this.showWarningMessage = true;
    }
  }

}
