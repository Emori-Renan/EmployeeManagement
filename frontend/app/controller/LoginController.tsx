import axios from "axios";
import { AuthError } from "../errors/AuthError";
import { handleApiError } from "../errors/handleApiError";
import type { AxiosError } from "axios";
import { AuthResponse } from "../types/userResponse";


interface LoginPayload {
    usernameOrEmail: string;
    password: string;
}

export const login = async (payload: LoginPayload): Promise<AuthResponse> => {
    try {
        const response = await axios.post<AuthResponse>("http://localhost:8080/auth/login", payload);
        return response.data;

    } catch (error: unknown) {
        const message = handleApiError(error);
        const status = (error as AxiosError).response?.status;
        throw new AuthError(message, status);

    }
};