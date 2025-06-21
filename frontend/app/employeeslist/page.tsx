"use client";
import { useEffect, useState, useCallback } from "react";
import LoadingModal from "../components/Loading";
import withAuth from "../store/withAuth";
import { getEmployees } from "../controller/EmployeeController";
import { getUsernameFromToken } from "../utils/auth";
import { useToast } from "../context/ToastContext";
import { useRouter } from 'next/navigation'

interface Employee  {
    id: string;
  employeeName: string;
  role: string;
  username: string;
}


const EmployeeList = () => {
    const router = useRouter();
    const { showToast } = useToast();
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchEmployees = useCallback(async (username: string) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await getEmployees(username);
            if (!response.success) {
                throw new Error(response.message);
            } 
            setEmployees(response.data ?? []);
        } catch  {
            showToast("Failed to load employees", "error");
        } finally {
            setIsLoading(false);
        }
    }, [showToast]);

    useEffect(() => {
        const name = getUsernameFromToken();
        if (name) {
            fetchEmployees(name);
        }
    }, [fetchEmployees]);

    return (
        <div className="hero bg-base-200 min-h-screen">
            <div className="hero-content flex-col">
                <h1 className="text-4xl font-bold mb-4">Your Employees</h1>

                {error && <p className="text-red-500 mb-4">{error}</p>}

                <div className="overflow-x-auto w-full max-w-md">
                    <table className="table table-zebra">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Role</th>
                            </tr>
                        </thead>
                        <tbody>
                            {employees.map((emp) => (
                                <tr key={emp.id}>
                                    <td>{emp.employeeName}</td>
                                    <td>{emp.role}</td>
                                    <td>
                                        <button onClick={() => router.push(`/employee/${emp.id}`)} className="btn btn-primary">Manage employee</button>
                                    </td>
                                </tr>
                                
                            ))}
                        </tbody>
                    </table>
                </div>
                <button onClick={() => router.push("/employeesregister")} className="btn btn-primary">Register employee</button>
                

                {employees.length === 0 && !isLoading && (
                    <p className="mt-4 text-gray-500">No employees registered yet.</p>
                )}

            </div>

            <LoadingModal isLoading={isLoading} message="Loading employees..." />
        </div>
    );
};

export default withAuth(EmployeeList);
