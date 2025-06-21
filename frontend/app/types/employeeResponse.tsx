import { ApiErrorResponse, ApiResponse } from "./generalResponse";

export type Employee = {
  id: number;
  employeeName: string;
  role: string;
};

export type EmployeeResponse = ApiResponse<Employee> | ApiErrorResponse;