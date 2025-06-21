import { handleApiError } from "../errors/handleApiError";
import apiClient from "../utils/apiClient";


interface WorkplaceRegister {
  workplaceName: string;
    hourlyWage: number;
    overtimeMultiplier: number;
}

export const workplaceRegistration = async (workplace: WorkplaceRegister) => {
  try {
    const token = localStorage.getItem("token");
    if (!token) {
      throw new Error("No token found");
    }

    const response = await apiClient.post<{ success: boolean; message?: string }>(
      "/workplace/register",
      workplace,
      {
        withCredentials: true,
      }
    );

    return response.data;
  } catch (error: unknown) {
    const errorMessage = handleApiError(error);
    return { success: false, message: errorMessage };
  }
}