import { Roles } from "./roles.model";

export class User {
    
        id?: any;
        username?: string;
        email?: string;
        roles?: Roles[];
        enabled?: boolean;
        locked?: boolean;
  }