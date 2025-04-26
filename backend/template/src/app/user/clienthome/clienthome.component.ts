import { Component, OnInit } from '@angular/core';
import { AppLayout } from "../../layout/component/app.layout";
import { TokenService } from '../../token-service/token.service';



@Component({
  selector: 'app-clienthome',
  imports: [AppLayout],
  templateUrl: './clienthome.component.html',
  styleUrl: './clienthome.component.scss',
  standalone:true
})
export class ClienthomeComponent implements OnInit {

    constructor(private tokenService: TokenService) {
    }
ngOnInit(){
    console.log("email : " + this.tokenService.getUserEmail());
    console.log("user id : " + this.tokenService.getUserId());
    console.log("organization id : " + this.tokenService.getOrganizationId());
}


}
