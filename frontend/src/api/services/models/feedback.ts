/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { Organization } from '../models/organization';
import { User } from '../models/user';
export interface Feedback {
  comment?: string;
  createdDate?: string;
  feedbackId?: number;
  lastModifiedDate?: string;
  note?: number;
  organisation?: Organization;
  read?: boolean;
  user?: User;
}
