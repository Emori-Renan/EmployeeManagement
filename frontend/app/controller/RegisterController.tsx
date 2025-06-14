import axios from "axios";
import { ApiError } from "next/dist/server/api-utils";

interface RegisterPayload {
    email: string;
    username: string;
    password: string;
    role: string;
}


export const register = async (payload: RegisterPayload) => {
    try {
        const response = await axios.post<{success: boolean; message: string}>("http://localhost:8080/auth/register", payload);
        return response.data;
    } catch (error: any) {
        if (error.response) {
          const message = error.response.data?.message || "An error occurred. Please try again.";
          return { success: false, message };
        } 
        else if (error.request) {
          return { success: false, message: "Network error, please check your connection." };
        } 
        else {
          return { success: false, message: "An unexpected error occurred." + error };
        }
    }
}