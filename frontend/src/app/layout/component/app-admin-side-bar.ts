import { Component } from "@angular/core";
import { AppAdminMenu } from "./app-admin-menu";

@Component({
    selector: 'app-admin-sidebar',
    standalone: true,
    imports: [AppAdminMenu],
    template: ` <div class="layout-sidebar">
        <app-admin-menu></app-admin-menu>
    </div>`
})
export class AppAdminSideBar {
}
