/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { ProjectProjection } from '../models/project-projection';
import { UserProjection } from '../models/user-projection';
export interface ProjectUserProjection {
  project?: ProjectProjection;
  role?: 'MANAGER' | 'TEAM_MEMBER';
  user?: UserProjection;
}
