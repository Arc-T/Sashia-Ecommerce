import type { User } from "./user";

export const AUTH_TOKEN_KEY = "authToken";

export interface AuthResponse {
  token: string;
}

export interface AuthContext {
  user?: User;
}
