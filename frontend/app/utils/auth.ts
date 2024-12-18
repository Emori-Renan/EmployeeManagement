import { jwtDecode, JwtPayload  } from "jwt-decode";

// /utils/auth.ts

export const saveToken = (token: string) => {
    localStorage.setItem('token', token);
};

export const getToken = () => {
    return localStorage.getItem('token');
};

export const clearToken = () => {
    localStorage.removeItem('token');
};


export function isTokenValid(token: string): boolean {
    try {
      const decoded = jwtDecode<JwtPayload>(token);
      if (!decoded.exp) return false; // Token doesn't have an expiration field
      const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds
      return decoded.exp > currentTime; // Check if token is still valid
    } catch (error) {
      console.error("Invalid token:", error);
      return false; // Token is invalid or couldn't be decoded
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