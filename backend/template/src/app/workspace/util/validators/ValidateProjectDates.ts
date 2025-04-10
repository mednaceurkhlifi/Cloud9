import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function validateProjectDates(): ValidatorFn {
    return (form: AbstractControl): ValidationErrors | null => {
        const beginDate = form.get('beginDateDate')?.value;
        const beginTime = form.get('beginDateTime')?.value;
        const deadlineDate = form.get('deadlineDate')?.value;
        const deadlineTime = form.get('deadlineTime')?.value;

        if (!beginDate || !beginTime || !deadlineDate || !deadlineTime) {
            return null;
        }

        const begin = new Date(
            beginDate.getFullYear(), beginDate.getMonth(), beginDate.getDate(),
            beginTime.getHours(), beginTime.getMinutes(), beginTime.getSeconds()
        );

        const deadline = new Date(
            deadlineDate.getFullYear(), deadlineDate.getMonth(), deadlineDate.getDate(),
            deadlineTime.getHours(), deadlineTime.getMinutes(), deadlineTime.getSeconds()
        );

        const now = new Date();

        const errors: any = {};

        if (begin < now) errors['beginInPast'] = true;
        if (deadline < now) errors['deadlineInPast'] = true;
        if (begin >= deadline) errors['invalidDateOrder'] = true;

        return Object.keys(errors).length ? errors : null;
    };
}
