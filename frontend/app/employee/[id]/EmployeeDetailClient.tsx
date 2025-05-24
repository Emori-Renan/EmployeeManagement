// app/employees/[id]/EmployeeDetailClient.tsx (modify existing file)
"use client";

import React, { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { workplaceRegistration } from "@/app/controller/WorkplaceController";
import WorkdayRegistrationModal from "@/app/components/WorkdayRegistrationModal";
import WorkdayViewerModal from "@/app/components/WorkdayViewerModal"; // Import the new modal
import apiClient from "@/app/utils/apiClient";

interface Employee {
  employeeName: string;
  id: number;
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
  const [isWorkplaceModalOpen, setIsWorkplaceModalOpen] = useState(false);
  const [isWorkdayRegistrationModalOpen, setIsWorkdayRegistrationModalOpen] = useState(false); // Renamed for clarity
  const [isWorkdayViewerModalOpen, setIsWorkdayViewerModalOpen] = useState(false); // New state for viewer modal

  // Functions for workplace modal
  const openWorkplaceModal = () => {
    setIsWorkplaceModalOpen(true);
  };

  const closeWorkplaceModal = () => {
    setIsWorkplaceModalOpen(false);
  };

  const handleRegisterWorkplace = async (workplace: Workplace) => {
    const response = await workplaceRegistration(workplace);
    if (!response.success) {
      setError(response.message || "Error registering workplace");
      return;
    }

    closeWorkplaceModal();
    alert(`Workplace "${workplace.workplaceName}" registered successfully!`);
    fetchEmployeeDetails(); // Refresh employee details, potentially workplaces list
  };

  // Functions for workday registration modal
  const openWorkdayRegistrationModal = () => {
    setIsWorkdayRegistrationModalOpen(true);
  };

  const closeWorkdayRegistrationModal = () => {
    setIsWorkdayRegistrationModalOpen(false);
  };

  const handleWorkdayRegistered = () => {
    console.log("Workday registered successfully in parent component.");
    // No need to fetch employee details here unless workdays are displayed directly on the main page.
    // The WorkdayViewerModal will fetch its own data.
  };

  // Functions for workday viewer modal (NEW)
  const openWorkdayViewerModal = () => {
    setIsWorkdayViewerModalOpen(true);
  };

  const closeWorkdayViewerModal = () => {
    setIsWorkdayViewerModalOpen(false);
  };


  const fetchEmployeeDetails = async () => {
    if (!id || typeof id !== "string") return;

    const token = localStorage.getItem("token");
    if (!token) {
      setError("No token found");
      return;
    }

    setError("");
    try {
      const response = await apiClient.get<{ success: boolean; message?: string; data: Employee }>(
        `/employee/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          withCredentials: true,
        }
      );

      if (response.data.success) {
        setEmployee(response.data.data);
      } else {
        setError(response.data.message || "Error fetching employee");
      }
    } catch (err: any) {
      if (err.response) {
        setError(err.response.data?.message || "Error fetching employee details.");
      } else if (err.request) {
        setError("Network error while fetching employee details.");
      } else {
        setError("An unexpected error occurred: " + err.message);
      }
    }
  };

  useEffect(() => {
    fetchEmployeeDetails();
  }, [id]);

  if (error) return <div className="text-red-500">Error: {error}</div>;
  if (!employee) return <div>Loading...</div>;

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">Employee Detail</h1>
      <p>
        <strong>Name:</strong> {employee.employeeName}
      </p>

      <div className="mt-4 flex gap-4">
        <button className="btn btn-primary" onClick={openWorkplaceModal}>
          Register Workplace
        </button>
        <button className="btn btn-secondary" onClick={openWorkdayRegistrationModal}>
          Register Workday
        </button>
        {/* NEW BUTTON */}
        <button className="btn btn-info" onClick={openWorkdayViewerModal}>
          View Workdays
        </button>
      </div>

      {/* Workplace Registration Modal */}
      {isWorkplaceModalOpen && (
        <dialog id="workplace_modal" className="modal modal-middle modal-open">
          <div className="modal-box">
            <h3 className="font-bold text-lg">Register Workplace</h3>
            <form
              onSubmit={(event) => {
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
              }}
            >
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
                <button type="button" className="btn btn-ghost" onClick={closeWorkplaceModal}>
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </dialog>
      )}

      {/* Workday Registration Modal */}
      {employee.id && (
        <WorkdayRegistrationModal
          isOpen={isWorkdayRegistrationModalOpen}
          onClose={closeWorkdayRegistrationModal}
          employeeId={employee.id}
          onWorkdayRegistered={handleWorkdayRegistered}
        />
      )}

      {/* NEW: Workday Viewer Modal */}
      {employee.id && (
        <WorkdayViewerModal
          isOpen={isWorkdayViewerModalOpen}
          onClose={closeWorkdayViewerModal}
          employeeId={employee.id}
        />
      )}
    </div>
  );
}