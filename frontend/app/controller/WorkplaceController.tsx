import apiClient from "../utils/apiClient";


interface Workplace {
  workplaceName: string;
    hourlyWage: number;
    overtimeMultiplier: number;
}

export const workplaceRegistration = async (workplace: Workplace) => {
  try {
    const token = localStorage.getItem("token");
    if (!token) {
      throw new Error("No token found");
    }

    const response = await apiClient.post<{ success: boolean; message?: string }>(
      "/workplace/register",
      workplace,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      }
    );

    return response.data;
  } catch (error: unknown) {
    if (error && typeof error === "object" && "response" in error) {
      const err = error as { response: { data?: { message?: string } } };
      const message = err.response.data?.message || "An error occurred. Please try again.";
      return { success: false, message };
    } else if (error && typeof error === "object" && "request" in error) {
      return { success: false, message: "Network error, please check your connection." };
    } else {
      return { success: false, message: "An unexpected error occurred." + String(error) };
    }
  }
}