import { Component } from '@angular/core';

import { AdminApplayout } from "../../layout/component/admin-applayout";
import { Dashboard } from "../../pages/dashboard/dashboard";
import { AppTopbar } from "../../layout/component/app.topbar";
import { AppSidebar } from "../../layout/component/app.sidebar";

@Component({
  selector: 'app-adminprofile',
  imports: [AppTopbar, AppSidebar],
  templateUrl: './adminprofile.component.html',
  styleUrl: './adminprofile.component.scss',
  standalone:true
})
export class AdminprofileComponent {

}
