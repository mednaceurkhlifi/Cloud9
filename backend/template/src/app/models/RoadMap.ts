import { User } from './User';
import { Step } from './Step';


        export class RoadMap {
                id: number|null;
                title: string;
                description: string;
                creator: User;
                nbrApprove:number;
                nbrDisapproval:number;
                steps: Step[]=[];
            
                constructor(init?: Partial<RoadMap>) {
                        this.id = init?.id ?? null;
                        this.title=init?.title ??"" ; 
                        this.description=init?.description ?? "";
                        this.nbrApprove=init?.nbrApprove ?? 0;
                        this.nbrDisapproval=init?.nbrDisapproval ?? 0;
                        this.creator=init?.creator ?? new User() ;
                        this.steps = (init?.steps ?? []).map(step => new Step(step));
                    }
            }