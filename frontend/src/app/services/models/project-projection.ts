/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { ProjectDocument } from '../models/project-document';
export interface ProjectProjection {
  achievement?: number;
  beginDate?: string;
  deadline?: string;
  description?: string;
  documents?: Array<ProjectDocument>;
  image?: string;
  name?: string;
  priority?: number;
  projectId?: number;
  status?: 'NOT_STARTED' | 'IN_PROGRESS' | 'ON_HOLD' | 'CANCELED' | 'UNDER_REVIEW' | 'FINISHED';
}
