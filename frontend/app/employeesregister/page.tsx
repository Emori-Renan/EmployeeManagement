"use client"

import { useState } from "react";
import {  useRouter } from "next/navigation";
import {  registerEmployee } from "@/app/controller/EmployeeController";
import LoadingModal from "@/app/components/Loading";
import { useToast } from "@/app/context/ToastContext";
import { getUsernameFromToken } from "../utils/auth";

const EmployeeDetail = () => {
    const router = useRouter();
    const { showToast } = useToast();

    const [employeeName, setEmployeeName] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const handleSave = async () => {
        setIsLoading(true);
        try {
            const usernameLoggedIn = getUsernameFromToken();
            if (!usernameLoggedIn) {
                showToast("You must be logged in to create an employee", "error");
                return;
            }
            await registerEmployee({ username: usernameLoggedIn, role: "employee", employeeName: employeeName });
            showToast("Employee created!", "success");
            router.push("/employeeslist");
        } catch  {
            showToast("Creation failed", "error");
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
            <button className="btn btn-primary" onClick={handleSave}>Save</button>

            <LoadingModal isLoading={isLoading} message="Saving..." />
        </div>
    );
};

export default EmployeeDetail;
