import apiClient from "../utils/apiClient";
import { ApiError } from "../errors/ApiError";
import { handleApiError } from "../errors/handleApiError";
import { AxiosError } from "axios";
import { AuthError } from "../errors/AuthError";

interface RegisterPayload {
  employeeName: string;
  role: string;
  username: string;
}

type RegisterResponse = { success: boolean; message: string } | ApiError;

export const registerEmployee = async (payload: RegisterPayload):Promise<RegisterResponse> => {
  try {
    const response = await apiClient.post<{ success: boolean; message: string }>("/employee/register", payload, { withCredentials: true });
    return response.data;
  } catch (error: unknown) {
    const message = handleApiError(error);
    const status = (error as AxiosError).response?.status;
    throw new AuthError(message, status);
  }
}

interface Employee  {
  id: string;
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
  } catch (error: unknown) {
    return { success: false, message: handleApiError(error) };
  }
}

export const getEmployeeById = async (id: string) => {
  try {
    const response = await apiClient.get<{ success: boolean; data: Employee; message?: string }>(
      `/employee/${id}`,
      { withCredentials: true }
    );
    return { success: true, data: response.data.data };
  } catch (error: unknown) {
    const message = handleApiError(error);
    const status = (error as AxiosError).response?.status;
    throw new AuthError(message, status);
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
  } catch (error: unknown) {
    const message = handleApiError(error);
    const status = (error as AxiosError).response?.status;
    throw new AuthError(message, status);
  }
}