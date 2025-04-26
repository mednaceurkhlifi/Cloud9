import { Injectable } from '@angular/core';
import { User } from '../../forum/api';

@Injectable({
  providedIn: 'root'
})
export class CurrUserServiceService {

  constructor() { }
  getCurrUser() : User{
      const user :User={
        userId:2,
        birthDate:"1990-05-15",
        address:"123 Cloud Street, SkyCity",
        email:"john.doe@example.com",
        firstName:"john",
        fullName:"john Doe",
        lastName:"securePassword123"
      };
      return user;
  }
}
