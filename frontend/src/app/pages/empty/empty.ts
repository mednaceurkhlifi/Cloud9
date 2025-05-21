import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-empty',
    standalone: true,
    template: ` <div class="card">
        <div class="font-semibold text-xl mb-4">{{title}}</div>
        <ng-content></ng-content>
    </div>`
})
export class Empty {
    @Input()
    title='Empty Page'
}
