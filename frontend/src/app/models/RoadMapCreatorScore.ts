import { User } from "./User";

export class RoadMapCreatorScore {
    user: User;
    TotlaNbrRoadMap: number;
    TotaleNbrApprovalls: number;
    TotaleNbrFollow: number;
    score: number; // Using number instead of Long as TypeScript doesn't have Long by default

    constructor(init?: Partial<RoadMapCreatorScore>) {
        this.user = init?.user ?? new User();
        this.TotlaNbrRoadMap = init?.TotlaNbrRoadMap ?? 0;
        this.TotaleNbrApprovalls = init?.TotaleNbrApprovalls ?? 0;
        this.TotaleNbrFollow = init?.TotaleNbrFollow ?? 0;
        this.score = init?.score ?? 0;
    }
}