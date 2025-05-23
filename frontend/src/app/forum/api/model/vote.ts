/**
 * Cloud nine - queute - ESPRIT PI 4ArcTic
 *
 * Contact: mohamedouerfelli3@gmail.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { User } from './user';
import { Votable } from './votable';


export interface Vote { 
    id?: number;
    voteType?: Vote.VoteTypeEnum;
    votable?: Votable;
    user?: User;
}
export namespace Vote {
    export type VoteTypeEnum = 'DOWNVOTE' | 'UPVOTE';
    export const VoteTypeEnum = {
        Downvote: 'DOWNVOTE' as VoteTypeEnum,
        Upvote: 'UPVOTE' as VoteTypeEnum
    };
}


