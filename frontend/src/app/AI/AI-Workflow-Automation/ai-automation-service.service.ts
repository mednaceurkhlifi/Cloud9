import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WorkFlowRequest } from '../WorkFlowRequest';
import { WorkFlowResponse } from '../workFlowResponse';

@Injectable({
    providedIn: 'root'
})
export class AiAutomationServiceService {

    url : string = "http://localhost:5678/";

    constructor(private _http:HttpClient) { }

    createProjectWithAi(request: WorkFlowRequest) : Observable<WorkFlowResponse> {
        return this._http.post(this.url + "webhook/f8cbac4b-18cd-438b-8ca8-907043f38245", request);
    }
}
