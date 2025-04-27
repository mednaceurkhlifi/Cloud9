import { Role } from "./enum/role.enum";

export class User {
    userId?: number;
    login?: string;
  password?: string;
  firstName?: string;
  lastName?: string;
  address?: string;
  dateOfBirth?: string;
  phoneNumber?: string;
  role?:Role;
  email?: string
}
