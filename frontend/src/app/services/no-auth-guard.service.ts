import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class NoAuthGuardService implements CanActivate {

  constructor(private auth:AuthenticationService, private router:Router) {}
  
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot){
      if(this.auth.isUserLoggedIn()){
        //  already logged in → redirect to task dashboard
        this.router.navigate(['/tasks']); 
        return false;
      }
      return true; // not logged in → allow access
    }
}
