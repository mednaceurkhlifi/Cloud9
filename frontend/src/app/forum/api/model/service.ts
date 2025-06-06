/**
 * Cloud nine - queute - ESPRIT PI 4ArcTic
 *
 * Contact: mohamedouerfelli3@gmail.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { Office } from './office';
import { Feedback } from './feedback';


export interface Service { 
    id?: number;
    serviceName?: string;
    type?: string;
    description?: string;
    feedbacks?: Array<Feedback>;
    office?: Office;
}

