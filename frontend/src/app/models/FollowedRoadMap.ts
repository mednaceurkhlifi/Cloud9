import { StepProgress } from "./StepProgress";
import { User } from "./User";

export class FollowedRoadMap {
    id: number | null;
    frTitle:string;
    frDescription: string;
    roadMapId: number;  
    user:User;
    stepProgressList: StepProgress[];

    constructor(init?: Partial<FollowedRoadMap>) {
        this.id = init?.id ?? null;
        this.frTitle = init?.frTitle ?? "";
        this.frDescription = init?.frDescription ?? "";
        this.roadMapId = init?.roadMapId ?? 0;
        this.user = init?.user ?? new User();
        this.stepProgressList = (init?.stepProgressList ?? []).map(
            progress => new StepProgress(progress)
        );
    }
}