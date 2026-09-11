import ApiClient from "@/lib/api-client";
import type { AuthResponse } from "@/types/auth";
import { api } from "@/types/endpoints";
import type { User, UserLogin } from "@/types/user";

export class UserService {
  static validateUserFromSession() {
    return ApiClient.for<User>(api.authentication.validate)
      .onError("validation_failed")
      .post();
  }

  static logout() {
    return ApiClient.for(api.authentication.logout)
      .onSuccess("logout_success")
      .post();
  }

  static authenticate(user: Partial<UserLogin>) {
    return ApiClient.for<AuthResponse>(api.authentication.login)
      .onSuccess("login_success")
      .post(user);
  }
}
