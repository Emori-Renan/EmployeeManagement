import { jwtDecode, JwtPayload  } from "jwt-decode";

export const saveToken = (token: string):void => {
    localStorage.setItem('token', token);
};

export const saveUsername = (username: string):void => {
    localStorage.setItem('username', username);
};

export const getToken = () => {
    return localStorage.getItem('token');
};

export const getUsername = () => {
    return localStorage.getItem('username');
};

export const clearToken = () => {
    localStorage.removeItem('token');
};


export function isTokenValid(token: string): boolean {
    try {
      const decoded = jwtDecode<JwtPayload>(token);
      if (!decoded.exp) return false;
      const currentTime = Math.floor(Date.now() / 1000); 
      return decoded.exp > currentTime; 
    } catch (error) {
      console.error("Invalid token:", error);
      return false; 
    }
  }

 export const getUsernameFromToken = () => {
    const token = localStorage.getItem('token');
    if (!token) {
      return null; 
    }
    try {
      const decodedToken = jwtDecode<JwtPayload>(token); 
      
      return decodedToken.sub ?? null; 
    } catch (error) {
      console.error('Invalid token:', error);
      return null; 
    }
  };