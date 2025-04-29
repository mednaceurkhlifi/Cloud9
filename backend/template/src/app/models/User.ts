import { Role } from "./enum/role.enum";

export class User {
    userId?: number;
    login?: string;
    password?: string;
    firstName?: string;
    lastName?: string;
    address?: string;
    birthDate?: string;
    created_at?: string;
    phoneNumber?: string;
    role?:Role;
    email?: string
    image?:string;
}
