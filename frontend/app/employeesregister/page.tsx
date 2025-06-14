"use client"

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { getEmployeeById, updateEmployee } from "@/app/controller/EmployeeController";
import LoadingModal from "@/app/components/Loading";
import { useToast } from "@/app/context/ToastContext";

const EmployeeDetail = () => {
    const { id } = useParams();
    const router = useRouter();
    const { showToast } = useToast();

    const [employeeName, setEmployeeName] = useState("");
    const [role, setRole] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true);
            try {
                const data = await getEmployeeById(id as string);
                if (data.data) {
                    setEmployeeName(data.data.employeeName);
                    setRole(data.data.role);
                } else {
                    showToast("Employee data is unavailable", "error");
                }
            } catch {
                showToast("Failed to load employee", "error");
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, [id]);

    const handleSave = async () => {
        setIsLoading(true);
        try {
            await updateEmployee(id as string, { employeeName, role, username: employeeName });
            showToast("Employee updated!", "success");
            router.push("/employees");
        } catch (e) {
            showToast("Update failed", "error");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="p-6 max-w-xl mx-auto">
            <h1 className="text-3xl font-bold mb-4">Edit Employee</h1>
            <label className="form-control mb-4">
                <span className="label-text">Name</span>
                <input
                    type="text"
                    className="input input-bordered"
                    value={employeeName}
                    onChange={(e) => setEmployeeName(e.target.value)}
                />
            </label>
            <label className="form-control mb-4">
                <span className="label-text">Role</span>
                <input
                    type="text"
                    className="input input-bordered"
                    value={role}
                    onChange={(e) => setRole(e.target.value)}
                />
            </label>
            <button className="btn btn-primary" onClick={handleSave}>Save</button>

            <LoadingModal isLoading={isLoading} message="Saving..." />
        </div>
    );
};

export default EmployeeDetail;
