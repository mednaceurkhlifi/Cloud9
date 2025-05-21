import { Component, OnInit } from '@angular/core';
import { User } from '../../models/User';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';

@Component({
  selector: 'app-oauth2-redirect',
  imports: [],
  templateUrl: './oauth2-redirect.component.html',
  styleUrl: './oauth2-redirect.component.scss',
  standalone:true
})
export class Oauth2RedirectComponent implements OnInit {

  response!:string;
  otherresponse!:string;
  connecteduser!:User;
constructor(private route: ActivatedRoute, private router: Router, private UserService:UserService) {}

ngOnInit(): void {
  this.route.queryParams.subscribe(params => {
    const email = params['email'];
    if (email) {
      localStorage.setItem('email', email);
      console.log("User email received from Google login");
      this.router.navigate(['/auth/client']);

    } else {
      console.error("No email found from Google redirect");
      this.router.navigate(['/auth/login']);
    }
  });
}
}
