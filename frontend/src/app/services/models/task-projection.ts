/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { ProjectDocument } from '../models/project-document';
export interface TaskProjection {
  beginDate?: string;
  deadline?: string;
  description?: string;
  documents?: Array<ProjectDocument>;
  priority?: number;
  status?: 'TO_DO' | 'IN_PROGRESS' | 'BLOCKED' | 'REWORK_NEEDED' | 'DONE' | 'ARCHIVED';
  taskId?: number;
  title?: string;
}
