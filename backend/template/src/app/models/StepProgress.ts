export class StepProgress {
    id: number | null;
    done: boolean;
    spDescription: string;
    spRequiredPaper: string;
    stepId: number;

    constructor(init?: Partial<StepProgress>) {
        this.id = init?.id ?? null;
        this.done = init?.done ?? false;
        this.spDescription = init?.spDescription ?? "";
        this.spRequiredPaper = init?.spRequiredPaper ?? "";
        this.stepId = init?.stepId ?? 0;
    }
}