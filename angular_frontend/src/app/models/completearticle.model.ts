import { User } from "./user.model";

export class CompleteArticle {

    id!: number;
    username!: string;
    image!: File;
    title!: string;
    content!: string;
    language!: string;
    published!: Date;
    lastEdited!: Date;

}