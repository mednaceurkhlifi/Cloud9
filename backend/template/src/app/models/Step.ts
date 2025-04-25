    export class Step {
        id: number|null;
        stepOrder: number;
        isStrict: boolean;
        stepName: string;
        stepDescription: string;
        requiredPapers: string;
        
        constructor(init?: Partial<Step>) {
            this.id = init?.id ?? null;
            this.stepOrder = init?.stepOrder ?? 0;
            this.isStrict = init?.isStrict ?? false;
            this.stepName = init?.stepName ?? '';
            this.stepDescription = init?.stepDescription ?? '';
            this.requiredPapers = init?.requiredPapers ?? '';
        }
      
    }
