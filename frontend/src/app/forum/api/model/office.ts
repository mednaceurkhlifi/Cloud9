/**
 * Cloud nine - queute - ESPRIT PI 4ArcTic
 *
 * Contact: mohamedouerfelli3@gmail.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { Organization } from './organization';
import { Service } from './service';


export interface Office { 
    id?: number;
    name?: string;
    location?: string;
    phoneNumber?: string;
    organisation?: Organization;
    services?: Array<Service>;
}

