import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthenticationService } from '../services/authentication.service';
import { SweetAlertService } from '../services/alet/sweet-alert.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink,CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  isMenuOpen = false;

  constructor(public auth : AuthenticationService,private alert:SweetAlertService){}

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }
  logout(){
    this.auth.logout();
    this.alert.Toast.fire({
            icon: "success",
            title: "You have logged out"
          });
  }
}
