/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { Organization } from '../models/organization';
import { Project } from '../models/project';
export interface Workspace {
  createdAt?: string;
  description?: string;
  image?: string;
  locked?: boolean;
  name?: string;
  organization?: Organization;
  projects?: Array<Project>;
  updatedAt?: string;
  workspaceId?: number;
}
