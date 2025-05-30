"use client";

import React, { FormEvent, useState, useEffect } from "react";
import { useToast } from "../context/ToastContext"; 
import { downloadWorkdayReport, fetchWorkplacesByEmployeeId } from "../controller/WorkdayController"; // Import fetchWorkplacesByEmployeeId

// Define WorkplaceOption interface if not already globally available
interface WorkplaceOption {
  id: number;
  workplaceName: string;
}

interface Props {
  isOpen: boolean;
  onClose: () => void;
  employeeId: number; // The employee ID for whom to download the report
}

export default function WorkdayReportDownloadModal({
  isOpen,
  onClose,
  employeeId,
}: Props) {
  const { showToast } = useToast();

  // State for form inputs
  const [startDate, setStartDate] = useState<string>('');
  const [endDate, setEndDate] = useState<string>('');
  // We will now store the selected workplace ID, and then get its name for the download function
  const [selectedWorkplaceId, setSelectedWorkplaceId] = useState<string>(''); // Storing ID as string from select value
  const [workplaces, setWorkplaces] = useState<WorkplaceOption[]>([]); // State for dropdown options
  const [isLoadingWorkplaces, setIsLoadingWorkplaces] = useState<boolean>(false);
  const [isLoadingReport, setIsLoadingReport] = useState<boolean>(false); // Renamed from isLoading to avoid confusion

  // Helper to format date to YYYY-MM-DD
  const formatDate = (date: Date) => date.toISOString().split('T')[0];

  // Load default filters and workplaces when modal opens
  useEffect(() => {
    if (!isOpen) return;

    // Set default dates to current month's start/end
    const today = new Date();
    const defaultStartDate = formatDate(new Date(today.getFullYear(), today.getMonth(), 1));
    const defaultEndDate = formatDate(new Date(today.getFullYear(), today.getMonth() + 1, 0));

    setStartDate(defaultStartDate);
    setEndDate(defaultEndDate);
    setSelectedWorkplaceId(''); // Reset selected workplace on open

    const loadWorkplaces = async () => {
      setIsLoadingWorkplaces(true);
      // No direct error state needed here, use toast for feedback
      const response = await fetchWorkplacesByEmployeeId(employeeId);
      if (response.success && response.data) {
        setWorkplaces(response.data);
      } else {
        showToast(response.message || "Failed to load workplaces for filter.", "error");
        setWorkplaces([]); // Ensure workplaces array is empty on error
      }
      setIsLoadingWorkplaces(false);
    };

    loadWorkplaces();
  }, [isOpen, employeeId, showToast]); // Added showToast to dependencies

  if (!isOpen) return null;

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    // Basic client-side validation
    if (!startDate || !endDate) {
      showToast("Please select both Start Date and End Date.", "error");
      return;
    }

    setIsLoadingReport(true); // Start loading state for report download

    try {
      let workplaceNameForDownload: string | undefined;

      // If a workplace ID is selected, find its name
      if (selectedWorkplaceId) {
        const selectedWp = workplaces.find(wp => wp.id.toString() === selectedWorkplaceId);
        workplaceNameForDownload = selectedWp ? selectedWp.workplaceName : undefined;
        if (!workplaceNameForDownload) {
          showToast("Selected workplace not found. Please try again.", "error");
          setIsLoadingReport(false);
          return;
        }
      } else {
        workplaceNameForDownload = undefined; // No specific workplace selected, so send undefined
      }

      // Call the downloadWorkdayReport function with the workplace name
      const result = await downloadWorkdayReport(
        employeeId,
        startDate,
        endDate,
        workplaceNameForDownload
      );

      if (result.success) {
        showToast(result.message || "Report download initiated successfully!", "success");
        onClose(); // Close modal on successful download
      } else {
        showToast(result.message || "Failed to download report.", "error");
      }
    } catch (error: any) {
      console.error("Error during report download:", error);
      showToast("An unexpected error occurred during report download.", "error");
    } finally {
      setIsLoadingReport(false); // End loading state
    }
  };

  return (
    <dialog id="report_modal" className="modal modal-middle modal-open">
      <div className="modal-box">
        <h3 className="font-bold text-lg">Download Workday Report</h3>
        <form onSubmit={handleSubmit}>
          <div className="form-control">
            <label className="label">
              <span className="label-text">Start Date</span>
            </label>
            <input
              type="date"
              className="input input-bordered"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
              required
            />
          </div>
          <div className="form-control mt-4">
            <label className="label">
              <span className="label-text">End Date</span>
            </label>
            <input
              type="date"
              className="input input-bordered"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
              required
            />
          </div>
          <div className="form-control mt-4">
            <label className="label">
              <span className="label-text">Workplace (Optional)</span>
            </label>
            {isLoadingWorkplaces ? (
              <span className="loading loading-spinner loading-md mx-auto"></span> // Centered spinner
            ) : (
              <select
                name="workplaceId" // Name attribute for consistency, but we use state
                className="select select-bordered w-full"
                value={selectedWorkplaceId}
                onChange={(e) => setSelectedWorkplaceId(e.target.value)}
              >
                <option value="">All Workplaces</option> {/* Option to select no filter */}
                {workplaces.map((wp) => (
                  <option key={wp.id} value={wp.id.toString()}>
                    {wp.workplaceName}
                  </option>
                ))}
              </select>
            )}
          </div>
          <div className="modal-action mt-6">
            <button type="submit" className="btn btn-primary" disabled={isLoadingReport}>
              {isLoadingReport ? "Downloading..." : "Download Report"}
            </button>
            <button type="button" className="btn btn-ghost" onClick={onClose} disabled={isLoadingReport}>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </dialog>
  );
}
