import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function validateDateRange(
    beginDateKey: string,
    beginTimeKey: string,
    endDateKey: string,
    endTimeKey: string
): ValidatorFn {
    return (form: AbstractControl): ValidationErrors | null => {
        const beginDate = form.get(beginDateKey)?.value;
        const beginTime = form.get(beginTimeKey)?.value;
        const endDate = form.get(endDateKey)?.value;
        const endTime = form.get(endTimeKey)?.value;

        if (!beginDate || !beginTime || !endDate || !endTime) {
            return null;
        }

        const begin = new Date(
            beginDate.getFullYear(), beginDate.getMonth(), beginDate.getDate(),
            beginTime.getHours(), beginTime.getMinutes(), beginTime.getSeconds()
        );

        const end = new Date(
            endDate.getFullYear(), endDate.getMonth(), endDate.getDate(),
            endTime.getHours(), endTime.getMinutes(), endTime.getSeconds()
        );

        const now = new Date();
        const errors: any = {};

        if (begin < now) errors['beginInPast'] = true;
        if (end < now) errors['deadlineInPast'] = true;
        if (begin >= end) errors['invalidDateOrder'] = true;

        return Object.keys(errors).length ? errors : null;
    };
}
