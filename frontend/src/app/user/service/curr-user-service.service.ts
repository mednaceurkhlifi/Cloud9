import { Injectable } from '@angular/core';
import { TokenService } from '../../token-service/token.service';
import { UserService } from '../user.service';
import { User } from '../../models/User';

@Injectable({
    providedIn: 'root'
})
export class CurrUserServiceService {

    constructor(private tokenService: TokenService,private userService: UserService) { }
    getCurrUser() : User{
        var id = this.tokenService.getUserId();
        var name = this.tokenService.getUserFullName();
        const user : User={
            userId:parseInt(id),
            firstName:name
        }
        return user;
    }
}
