/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { Organization } from '../models/organization';
export interface User {
  address?: string;
  barCode?: string;
  birth_date?: string;
  created_at?: string;
  email?: string;
  enabled?: boolean;
  first_name?: string;
  full_name?: string;
  image?: string;
  is_deleted?: boolean;
  is_locked?: boolean;
  last_name?: string;
  organization?: Organization;
  password?: string;
  phone_number?: string;
  reset_pwd_code?: string;
  reset_pwd_date?: string;
  role?: 'CLIENT' | 'ADMIN' | 'STAFF';
  updated_at?: string;
  userId?: number;
}
