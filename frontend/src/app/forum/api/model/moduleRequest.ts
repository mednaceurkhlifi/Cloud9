/**
 * Cloud nine - queute - ESPRIT PI 4ArcTic
 *
 * Contact: mohamedouerfelli3@gmail.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { TaskRequest } from './taskRequest';


export interface ModuleRequest { 
    title?: string;
    description?: string;
    priority?: number;
    achievement?: number;
    beginDate?: string;
    deadline?: string;
    tasks?: Array<TaskRequest>;
}

