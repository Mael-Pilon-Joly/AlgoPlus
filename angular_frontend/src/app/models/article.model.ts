import { User } from "./user.model";

export class Article {
    user: User | undefined;
    title!: string;
    content!: string;
    language!: string
}