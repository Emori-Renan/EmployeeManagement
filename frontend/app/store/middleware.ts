import { Middleware } from "@reduxjs/toolkit";
import { saveToken, clearToken } from "../utils/auth";

export const authMiddleware: Middleware = () => (next) => (action) => {
  const result = next(action);

  if (typeof action === "object" && action !== null && "type" in action) {
    const typedAction = action as { type: string; payload?: string };

    if (typedAction.type === "auth/loginSuccess" && typeof typedAction.payload === "string") {
      saveToken(typedAction.payload);
    } else if (typedAction.type === "auth/logout") {
      clearToken();
    }
  }

  return result;
};