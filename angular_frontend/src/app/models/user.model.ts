import { CompleteArticle } from "./completearticle.model";
import { Roles } from "./roles.model";

export class User {
    
        id?: any;
        username?: string;
        email?: string;
        avatar?: any;
        cv?: any;
        roles?: Roles[];
        enabled?: boolean;
        locked?: boolean;
        articles?: CompleteArticle[]
  }