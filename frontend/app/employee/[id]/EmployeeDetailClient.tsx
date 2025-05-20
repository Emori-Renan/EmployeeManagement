// app/employees/[id]/EmployeeDetailClient.tsx
"use client";

import React, { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { workplaceRegistration } from "@/app/controller/WorkplaceController";

interface Employee {
  employeeName: string;
  // Add other fields as needed, e.g. id: string; email: string; etc.
}

interface Workplace {
  workplaceName: string;
  hourlyWage: number;
  overtimeMultiplier: number;
  employeeId: number;
}


export default function EmployeeDetailClient() {
  const { id } = useParams();
  const [employee, setEmployee] = useState<Employee | null>(null);
  const [error, setError] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleRegisterWorkplace = async (workplace: Workplace) => {
    // Here you would implement the logic to send the workplace data
    // to your backend, potentially associating it with the current employee ID.
    const response = await workplaceRegistration(workplace);
    if (!response.success) {
      setError(response.message || "Error registering workplace");
      return;
    }
    
    closeModal();
    alert(`Workplace "${workplace.workplaceName}" registered (console logged)!`);
    // In a real application, you would likely make a POST request here.
  };

  useEffect(() => {
    if (!id || typeof id !== "string") return;

    const token = localStorage.getItem("token");
    if (!token) {
      setError("No token found");
      return;
    }

    fetch(`http://localhost:8080/employee/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(async (res) => {
        const data = await res.json();
        if (!res.ok) {
          setError(data.message || "Error fetching employee");
        } else {
          setEmployee(data.data);
        }
      })
      .catch(() => setError("Network error"));
  }, [id]);

  if (error) return <div className="text-red-500">Error: {error}</div>;
  if (!employee) return <div>Loading...</div>;

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">Employee Detail</h1>
      <p><strong>Name:</strong> {employee.employeeName}</p>

      <button className="btn btn-primary mt-4" onClick={openModal}>
        Register Workplace
      </button>

      {isModalOpen && (
        <dialog id="workplace_modal" className="modal modal-middle modal-open">
          <div className="modal-box">
            <h3 className="font-bold text-lg">Register Workplace</h3>
            <form onSubmit={(event) => {
              event.preventDefault();
              const formData = new FormData(event.currentTarget);
              const workplaceName = formData.get("name") as string;
              const hourlyWageStr = formData.get("hourlyWage") as string;
              const overtimeMultiplierStr = formData.get("overtimeMultiplier") as string;

              if (workplaceName && hourlyWageStr && overtimeMultiplierStr) {
                const hourlyWage = parseFloat(hourlyWageStr);
                const overtimeMultiplier = parseFloat(overtimeMultiplierStr);
                if (!isNaN(hourlyWage) && !isNaN(overtimeMultiplier) && hourlyWage > 0 && overtimeMultiplier > 0) {
                  handleRegisterWorkplace({ workplaceName, hourlyWage, overtimeMultiplier, employeeId: Number(id) });
                } else {
                  alert("Please enter valid numeric values for Hourly Wage and Overtime Multiplier.");
                }
              } else {
                alert("Please fill in all fields.");
              }
            }}>
              <div className="form-control">
                <label className="label">
                  <span className="label-text">Name</span>
                </label>
                <input
                  type="text"
                  placeholder="Workplace Name"
                  className="input input-bordered"
                  name="name"
                  required
                />
              </div>
              <div className="form-control mt-4">
                <label className="label">
                  <span className="label-text">Hourly wage</span>
                </label>
                <input
                  type="number"
                  placeholder="Enter hourly wage"
                  className="input input-bordered"
                  name="hourlyWage"
                  required
                />
              </div>
              <div className="form-control mt-4">
                <label className="label">
                  <span className="label-text">Overtime Multiplier</span>
                </label>
                <input
                  type="number"
                  placeholder="e.g., 1.5"
                  className="input input-bordered"
                  name="overtimeMultiplier"
                  step="0.01" 
                  required
                />
              </div>
              <div className="modal-action mt-6">
                <button type="submit" className="btn btn-primary">
                  Register
                </button>
                <button type="button" className="btn btn-ghost" onClick={closeModal}>
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </dialog>
      )}
    </div>
  );
}