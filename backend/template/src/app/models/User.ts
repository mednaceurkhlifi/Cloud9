export class User {
        userId?: number |null ;
        firstName?: string;
        lastName?: string;
   
       constructor(init?: Partial<User>) {
           this.userId =init?.userId ?? null;
           this.firstName = init?.firstName ?? '';
           this.lastName =init?.lastName ?? '';
       }
}