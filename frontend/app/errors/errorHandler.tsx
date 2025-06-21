import { AxiosError } from "axios";
import { handleApiError } from "./handleApiError";
import { AuthError } from "./AuthError";

export const handleException = (error: unknown): never => {
    const message = handleApiError(error);
    const status = (error as AxiosError).response?.status;
    throw new AuthError(message, status);
};