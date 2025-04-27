export class StepProgress {
    id: number | null;
    isDone: boolean;
    spDescription: string;
    spRequiredPaper: string;
    stepId: number;

    constructor(init?: Partial<StepProgress>) {
        this.id = init?.id ?? null;
        this.isDone = init?.isDone ?? false;
        this.spDescription = init?.spDescription ?? "";
        this.spRequiredPaper = init?.spRequiredPaper ?? "";
        this.stepId = init?.stepId ?? 0;
    }
}