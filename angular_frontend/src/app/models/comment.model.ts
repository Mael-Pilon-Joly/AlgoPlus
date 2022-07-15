export class CommentModel {
    id!: number;
    title!: string;
    user_avatar!: File;
    published!: Date;
    lastEdited!: Date;
    content!: string;
    username!:string;
    usernameId!: number;
}