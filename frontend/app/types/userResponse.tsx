export interface UserResponse {
  success: boolean;
    message: string;
}

export interface AuthSuccessResponse {
  token: string;
  message: string;
}

export interface AuthErrorResponse {
  token: null;
  message: string;
}

export type AuthResponse = AuthSuccessResponse | AuthErrorResponse;
