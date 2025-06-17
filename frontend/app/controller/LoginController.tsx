import axios from "axios";
import { AuthError } from "../errors/AuthError";
import { handleApiError } from "../errors/handleApiError";
import type { AxiosError } from "axios";


interface LoginPayload {
    usernameOrEmail: string;
    password: string;
}

interface LoginResponse {
    token?: string;
    message: string; 
}

export const login = async (payload: LoginPayload): Promise<LoginResponse> => {

    try {
        const response = await axios.post<LoginResponse>("http://localhost:8080/auth/login", payload);
        console.log(response.status)
        if (response.status === 200 ) {
            console.log("Login successful", response.data);
            return response.data; 
        } else {
            throw new AuthError("Login failed", response.status);
        }
    } catch (error: unknown) {
        const message = handleApiError(error);
        const status = (error as AxiosError).response?.status;
        throw new AuthError(message, status);

    }
};