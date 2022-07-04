import { HttpStatusCode } from "@angular/common/http";
import { User } from "./user.model";

export class SignUpResponse {
  error?: {
  user?: User;
  httpStatus?: string[]
  }
}