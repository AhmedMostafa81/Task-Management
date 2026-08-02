import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthenticationService } from './authentication.service';

export const HttpInterceptorService: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthenticationService);
  const token = authService.getToken();

  // If we have a token, clone the request, add the header, and pass the clone
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: token // Looks like: "Bearer eyJhbGciOi..."
      }
    });
    return next(authReq);
  }

  // Otherwise, pass the original request untouched (e.g., for /login or /register)
  return next(req);
};