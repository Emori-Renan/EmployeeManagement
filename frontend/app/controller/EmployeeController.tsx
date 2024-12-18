import apiClient from "../utils/apiClient";


interface RegisterPayload {
  employeeName: string;
  role: string;
  username: string;
}

export const registerEmployee = async (payload: RegisterPayload) => {
  try {
    const response = await apiClient.post<{ success: boolean; message: string }>("/employee/register", payload, { withCredentials: true });
    console.log(response);

    return response.data;
  } catch (error: any) {
    // Check if it's a response error (HTTP status code errors)
    if (error.response) {
      // Server responded with a status other than 2xx
      const message = error.response.data?.message || "An error occurred. Please try again.";
      return { success: false, message };
    }
    // Check if it's a network error (request never completed or was blocked)
    else if (error.request) {
      return { success: false, message: "Network error, please check your connection." };
    }
    // Handle any other errors that may have occurred
    else {
      return { success: false, message: "An unexpected error occurred." + error };
    }
  }
}