import apiClient from "../utils/apiClient"; 

interface WorkdayRegistrationData {
  employeeId: number;
  workplaceId: number;
  date: string; 
  hoursWorked: number;
  overtimeHours: number;
  transportCost: number;
}

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
      `/workplace/${employeeId}`,
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
  date: string;
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


export const downloadWorkdayReport = async (
  employeeId: number,
  startDate: string,
  endDate: string,
  workplace?: string 
): Promise<ServiceResponse<void>> => {
  try {
    const token = localStorage.getItem("token");
    if (!token) {
      return { success: false, message: "Authentication token not found." };
    }

    const queryParams = new URLSearchParams({
      startDate: startDate,
      endDate: endDate,
    });

    if (workplace) {
      queryParams.append("workplace", workplace);
    }

    const url = `/workday-report/${employeeId}?${queryParams.toString()}`;

    const response = await apiClient.get(url, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' 
      },
      responseType: 'blob', 
      withCredentials: true,
    });

    const contentDisposition = response.headers['content-disposition'];
    let filename = `workday_report_${employeeId}.xlsx`;
    if (contentDisposition) {
      const filenameMatch = contentDisposition.match(/filename="([^"]+)"/);
      if (filenameMatch && filenameMatch[1]) {
        filename = filenameMatch[1];
      }
    }

    const blob = new Blob([response.data as BlobPart], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const downloadUrl = window.URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = downloadUrl;
    link.setAttribute('download', filename); 
    document.body.appendChild(link);
    link.click(); 
    link.remove(); 
    window.URL.revokeObjectURL(downloadUrl); 

    return { success: true, message: "Workday report downloaded successfully!" };

  } catch (error: any) {
    console.error("Error downloading workday report:", error);
    if (error.response) {
      try {
        const errorData = JSON.parse(await error.response.data.text());
        const message = errorData.message || "Failed to download report.";
        return { success: false, message };
      } catch (parseError) {
        const message = error.response.data?.message || "Failed to download report. Server responded with an error.";
        return { success: false, message };
      }
    } else if (error.request) {
      return { success: false, message: "Network error, please check your connection." };
    } else {
      return { success: false, message: "An unexpected error occurred: " + error.message };
    }
  }
};
