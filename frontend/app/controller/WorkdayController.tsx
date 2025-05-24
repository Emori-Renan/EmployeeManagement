// app/controller/WorkdayController.ts

import apiClient from "../utils/apiClient";

interface WorkdayRegistrationData {
  employeeId: number;
  workplaceId: number;
  date: string; // Use ISO 8601 format (YYYY-MM-DD)
  hoursWorked: number;
  overtimeHours: number;
  transportCost: number;
}

// Define the expected structure of a successful API response from your backend
interface ServiceResponse<T> {
  success: boolean;
  message?: string;
  data?: T;
}

export const workdayRegistration = async (
  workdayData: WorkdayRegistrationData
): Promise<ServiceResponse<void>> => {
  try {
    const token = localStorage.getItem("token");
    if (!token) {
      return { success: false, message: "Authentication token not found." };
    }

    const response = await apiClient.post<ServiceResponse<any>>(
      "/workday/register", 
      workdayData,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      }
    );

    return response.data;
  } catch (error: any) {
    if (error.response) {
      const message = error.response.data?.message || "An error occurred. Please try again.";
      return { success: false, message };
    } else if (error.request) {
      return { success: false, message: "Network error, please check your connection." };
    } else {
      return { success: false, message: "An unexpected error occurred: " + error.message };
    }
  }
};


interface WorkplaceOption {
  id: number;
  workplaceName: string;
}

export const fetchWorkplacesByEmployeeId = async (employeeId: number): Promise<ServiceResponse<WorkplaceOption[]>> => {
  try {
    const token = localStorage.getItem("token");
    if (!token) {
      return { success: false, message: "Authentication token not found." };
    }

    const response = await apiClient.get<ServiceResponse<WorkplaceOption[]>>(
      `/workplace/${employeeId}`, // This was already corrected to match your WorkplaceController
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      }
    );

    return response.data;
  } catch (error: any) {
    if (error.response) {
      const message = error.response.data?.message || "Failed to fetch workplaces.";
      return { success: false, message };
    } else if (error.request) {
      return { success: false, message: "Network error while fetching workplaces." };
    } else {
      return { success: false, message: "An unexpected error occurred while fetching workplaces: " + error.message };
    }
  }
};

interface WorkdayDisplayData {
  id: number;
  date: string; // Or LocalDate if you prefer handling dates as objects
  workplaceName: string;
  hoursWorked: number;
  overtimeHours: number;
  transportCost: number;
  employeeId: number;
  workplaceId: number;
}

interface WorkdayFilterParams {
  startDate?: string;
  endDate?: string;
  workplaceId?: number;
}

export const getWorkdaysByEmployeeAndFilters = async (
  employeeId: number,
  filters: WorkdayFilterParams
): Promise<ServiceResponse<WorkdayDisplayData[]>> => {
  try {
    const token = localStorage.getItem("token");
    if (!token) {
      return { success: false, message: "Authentication token not found." };
    }

    // Build query parameters
    const queryParams = new URLSearchParams();
    if (filters.startDate) queryParams.append("startDate", filters.startDate);
    if (filters.endDate) queryParams.append("endDate", filters.endDate);
    if (filters.workplaceId) queryParams.append("workplaceId", filters.workplaceId.toString());

    const queryString = queryParams.toString();
    const url = `/workdays/${employeeId}/filtered` + (queryString ? `?${queryString}` : "");

    const response = await apiClient.get<ServiceResponse<WorkdayDisplayData[]>>(url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      withCredentials: true,
    });

    return response.data;
  } catch (error: any) {
    if (error.response) {
      const message = error.response.data?.message || "Failed to retrieve workdays.";
      return { success: false, message };
    } else if (error.request) {
      return { success: false, message: "Network error, please check your connection." };
    } else {
      return { success: false, message: "An unexpected error occurred: " + error.message };
    }
  }
};
