"use client";

import React, { useEffect, useState, useCallback } from "react";
import { useParams } from "next/navigation";
import { workplaceRegistration } from "@/app/controller/WorkplaceController";
import WorkdayRegistrationModal from "@/app/components/WorkdayRegistrationModal";
import WorkdayViewerModal from "@/app/components/WorkdayViewerModal";
import WorkplaceRegistrationModal from "@/app/components/WorkplaceRegistrationModal";
import { useToast } from "@/app/context/ToastContext";
import WorkdayReportDownloadModal from "@/app/components/WorkdayReportModal";
import { handleException } from "@/app/errors/errorHandler";
import { getEmployeeById } from "@/app/controller/EmployeeController";
import { Workplace } from "@/app/types/workplaceResponse";

interface Employee {
  employeeName: string;
  id: number;
}

export default function EmployeeDetailClient() {
  const { id } = useParams();
  const [employee, setEmployee] = useState<Employee | null>(null);
  const [error, setError] = useState("");
  const [isWorkplaceModalOpen, setIsWorkplaceModalOpen] = useState(false);
  const [isWorkdayRegistrationModalOpen, setIsWorkdayRegistrationModalOpen] = useState(false);
  const [isWorkdayViewerModalOpen, setIsWorkdayViewerModalOpen] = useState(false); 
  const { showToast } = useToast();
   const [isReportDownloadModalOpen, setIsReportDownloadModalOpen] = useState(false);
  const openReportDownloadModal = () => setIsReportDownloadModalOpen(true);
  const closeReportDownloadModal = () => setIsReportDownloadModalOpen(false);

  const openWorkplaceModal = () => {
    setIsWorkplaceModalOpen(true);
  };

  const closeWorkplaceModal = () => {
    setIsWorkplaceModalOpen(false);
  };

  const handleRegisterWorkplace = async (workplace: Workplace) => {
    const response = await workplaceRegistration(workplace);
    if (!response.success) {
      setError(response.message ?? "Error registering workplace");
      return;
    }

    closeWorkplaceModal();
    showToast(`Workplace "${workplace.workplaceName}" registered successfully!`, "success");
    fetchEmployeeDetails();
  };

  const openWorkdayRegistrationModal = () => {
    setIsWorkdayRegistrationModalOpen(true);
  };

  const closeWorkdayRegistrationModal = () => {
    setIsWorkdayRegistrationModalOpen(false);
  };

  const handleWorkdayRegistered = () => {
    console.log("Workday registered successfully in parent component.");
  };

  const openWorkdayViewerModal = () => {
    setIsWorkdayViewerModalOpen(true);
  };

  const closeWorkdayViewerModal = () => {
    setIsWorkdayViewerModalOpen(false);
  };
  
  const fetchEmployeeDetails = useCallback(async () => {
    if (!id || typeof id !== "string") return;
    const token = localStorage.getItem("token");
    if (!token) {
      setError("No token found");
      return;
    }
    setError("");
    try {
      const response = await getEmployeeById(id);

      if (response.success) {
        setEmployee(response.data);
      } else {
        setError(response.message ?? "Error fetching employee");
      }
    } catch (err: unknown) {
      handleException(err);
      showToast("An unexpected error occurred while fetching employee details.", "error");
    }
  }, [id, showToast]);

  useEffect(() => {
    fetchEmployeeDetails();
  }, [fetchEmployeeDetails]);

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
        <button className="btn btn-info" onClick={openWorkdayViewerModal}>
          View Workdays
        </button>
        <button className="btn btn-success" onClick={openReportDownloadModal}>
          Download Workday Report
        </button>
      </div>

      {!!employee.id && (
        <WorkplaceRegistrationModal
          isOpen={isWorkplaceModalOpen}
          onClose={closeWorkplaceModal}
          onRegister={handleRegisterWorkplace}
          employeeId={employee.id}
        />
      )}

      {!!employee.id && (
        <WorkdayRegistrationModal
          isOpen={isWorkdayRegistrationModalOpen}
          onClose={closeWorkdayRegistrationModal}
          employeeId={employee.id}
          onWorkdayRegistered={handleWorkdayRegistered}
        />
      )}


      {!!employee.id && (
        <WorkdayViewerModal
          isOpen={isWorkdayViewerModalOpen}
          onClose={closeWorkdayViewerModal}
          employeeId={employee.id}
        />
      )}

      {!!employee?.id && ( 
        <WorkdayReportDownloadModal
          isOpen={isReportDownloadModalOpen}
          onClose={closeReportDownloadModal}
          employeeId={employee.id}
        />
      )}
    </div>

    
  );
}