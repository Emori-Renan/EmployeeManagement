import apiClient from "../utils/apiClient";

interface RegisterPayload {
  employeeName: string;
  role: string;
  username: string;
}

export const registerEmployee = async (payload: RegisterPayload) => {
  try {
    const response = await apiClient.post<{ success: boolean; message: string }>("/employee/register", payload, { withCredentials: true });
    return response.data;
  } catch (error: unknown) {
    if (typeof error === "object" && error !== null && "response" in error) {
      const err = error as { response: { data?: { message?: string } } };
      const message = err.response.data?.message || "An error occurred. Please try again.";
      return { success: false, message };
    }
    else if (typeof error === "object" && error !== null && "request" in error) {
      return { success: false, message: "Network error, please check your connection." };
    }
    else {
      return { success: false, message: "An unexpected error occurred." + error };
    }
  }
}

interface Employee  {
  employeeName: string;
  role: string;
  username: string;
}

export const getEmployees = async (username: string) => {
  try {
    const response = await apiClient.get<{ success: boolean; data: Employee[]; message?: string }>(
      `/employee/list?username=${username}`,
      { withCredentials: true }
    );
    return { success: true, data: response.data.data };
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

export const getEmployeeById = async (id: string) => {
  try {
    const response = await apiClient.get<{ success: boolean; data: Employee; message?: string }>(
      `/employee/${id}`,
      { withCredentials: true }
    );
    return { success: true, data: response.data.data };
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

export const updateEmployee = async (id: string, payload: RegisterPayload) => {
  try {
    const response = await apiClient.put<{ success: boolean; message: string }>(
      `/employee/${id}`,
      payload,
      { withCredentials: true }
    );
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