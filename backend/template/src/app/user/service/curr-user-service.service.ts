import { Injectable } from '@angular/core';
import { User } from '../../forum/api';

@Injectable({
  providedIn: 'root'
})
export class CurrUserServiceService {

  constructor() { }
  getCurrUser() : User{
      const user :User={
        user_id:2,
        birth_date:"1990-05-15",
        address:"123 Cloud Street, SkyCity",
        email:"john.doe@example.com",
        firs_name:"john",
        full_name:"john Doe",
        last_name:"securePassword123"
      };
      return user;
  }
}
