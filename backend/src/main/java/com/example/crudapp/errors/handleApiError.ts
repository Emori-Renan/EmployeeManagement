// utils/handleApiError.ts
import axios from "axios";
import type { AxiosError } from "axios";

export const handleApiError = (error: unknown): string => {
  if ((axios as any).isAxiosError && (axios as any).isAxiosError(error)) {
    const axiosError = error as AxiosError;
    const status = axiosError.response?.status;

    switch (status) {
      case 400:
        return (axiosError.response?.data as { message?: string })?.message || "Invalid input.";
      case 401:
        return "Session expired. Please log in again.";
      case 403:
        return "You don’t have permission to do that.";
      case 404:
        return "Requested resource not found.";
      case 500:
        return "Server error. Please try again later.";
      default:
        return axiosError.message || "An unknown error occurred.";
    }
  }

  return "Unexpected error. Please try again.";
};