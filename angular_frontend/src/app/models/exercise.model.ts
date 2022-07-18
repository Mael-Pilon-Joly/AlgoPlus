import { Optional } from "@angular/core";

export class Exercise {
    id: string | undefined;
    title!: string;
    username!: string;
    explanation!: string;
    image?: File | undefined;
    solutions!: Map<string, string>;
}