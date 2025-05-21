import { News } from "./News";
import { User } from "./User";

export class Reaction{
    reactionId?:Number;
    reactionType!:string;
    date!:Date;
    user!:User;
    news!:News;

}