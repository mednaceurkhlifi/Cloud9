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
import { News } from './news';


export interface Trending { 
    trendingId?: number;
    date?: string;
    type?: string;
    score?: number;
    active?: string;
    news?: News;
    user?: User;
}

