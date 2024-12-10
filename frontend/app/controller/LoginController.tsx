import axios from "axios";
import { AuthError } from "../errors/AuthError";

interface LoginPayload {
    usernameOrEmail: string;
    password: string;
}

interface LoginResponse {
    token?: string; // Token is present only when the login is successful
    message: string; // Error or success message
}

export const login = async (payload: LoginPayload) => {

    try {
        const response = await axios.post<LoginResponse>("http://localhost:8080/auth/login", payload);
        console.log(response.status)
        if (response.status === 200 ) {
            console.log("Login successful", response.data);
            return response.data; // Retorna os dados da resposta da API
        } else {
            throw new AuthError("Login failed", response.status);
        }
    } catch (error: any) {
        console.log("olha o erro ai", error.response.status);

        if (error.response.status === 404) {
            return new AuthError("User not found", 404);
        } else if (error.response.status === 401) {
            return new AuthError("Invalid credentials", 401);
        } else {
            return new AuthError("An unexpected error occurred", 500);
        }

    }
};