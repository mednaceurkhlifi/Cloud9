import { Routes } from "@angular/router";
import { StepperComponent } from "./stepper.component";

export default[
    {path:'', component:StepperComponent},
    { path: ':id', component: StepperComponent }

]satisfies Routes