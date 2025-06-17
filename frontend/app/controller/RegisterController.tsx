import axios from "axios";
import { UserResponse } from "../types/userResponse";

interface RegisterPayload {
    email: string;
    username: string;
    password: string;
    role: string;
}


export const register = async (payload: RegisterPayload):Promise<UserResponse> => {
    try {
        const response = await axios.post<UserResponse>("http://localhost:8080/auth/register", payload);
            return response.data;
    } catch (error: unknown) {
        if (axios.isAxiosError(error)) {
          if (error.response) {
            const message = error.response.data?.message ?? "An error occurred. Please try again.";
            return { success: false, message };
          } 
          else if (error.request) {
            return { success: false, message: "Network error, please check your connection." };
          } 
        }
        return { success: false, message: "An unexpected error occurred." + (error instanceof Error ? ` ${error.message}` : "") };
    }
}