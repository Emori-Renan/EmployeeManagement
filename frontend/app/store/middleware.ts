import { clearToken, saveToken } from "../utils/auth";

export const authMiddleware = (store) => (next) => (action) => {
    const result = next(action);
  
    if (action.type === 'auth/loginSuccess') {
      saveToken(action.payload)
    } else if (action.type === 'auth/logout') {
      clearToken();
    }
  
    return result;
  };
  