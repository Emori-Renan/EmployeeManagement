"use client";

import React, { FormEvent, useState, useEffect } from "react";
import { useToast } from "../context/ToastContext"; 
import { downloadWorkdayReport, fetchWorkplacesByEmployeeId } from "../controller/WorkdayController";

interface WorkplaceOption {
  id: number;
  workplaceName: string;
}

interface Props {
  readonly isOpen: boolean;
  readonly onClose: () => void;
  readonly employeeId: number; 
}

export default function WorkdayReportDownloadModal({
  isOpen,
  onClose,
  employeeId,
}: Props) {
  const { showToast } = useToast();

  const [startDate, setStartDate] = useState<string>('');
  const [endDate, setEndDate] = useState<string>('');
  const [selectedWorkplaceId, setSelectedWorkplaceId] = useState<string>(''); 
  const [workplaces, setWorkplaces] = useState<WorkplaceOption[]>([]); 
  const [isLoadingWorkplaces, setIsLoadingWorkplaces] = useState<boolean>(false);
  const [isLoadingReport, setIsLoadingReport] = useState<boolean>(false); 

  const formatDate = (date: Date) => date.toISOString().split('T')[0];

  useEffect(() => {
    console.log("WorkdayReportDownloadModal useEffect triggered");
    if (!isOpen) return;

    const today = new Date();
    const defaultStartDate = formatDate(new Date(today.getFullYear(), today.getMonth(), 1));
    const defaultEndDate = formatDate(new Date(today.getFullYear(), today.getMonth() + 1, 0));

    setStartDate(defaultStartDate);
    setEndDate(defaultEndDate);
    setSelectedWorkplaceId(''); 

    const loadWorkplaces = async () => {
      setIsLoadingWorkplaces(true);
      const response = await fetchWorkplacesByEmployeeId(employeeId);
      if (response.success && response.data) {
        setWorkplaces(response.data);
      } else {
        setWorkplaces([]); 
      }
      setIsLoadingWorkplaces(false);
    };

    loadWorkplaces();
  }, [isOpen, employeeId]);
  if (!isOpen) return null;

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (!startDate || !endDate) {
      showToast("Please select both Start Date and End Date.", "error");
      return;
    }

    setIsLoadingReport(true); 

    try {
      let workplaceNameForDownload: string | undefined;

      if (selectedWorkplaceId) {
        const selectedWp = workplaces.find(wp => wp.id.toString() === selectedWorkplaceId);
        workplaceNameForDownload = selectedWp ? selectedWp.workplaceName : undefined;
        if (!workplaceNameForDownload) {
          showToast("Selected workplace not found. Please try again.", "error");
          setIsLoadingReport(false);
          return;
        }
      } else {
        workplaceNameForDownload = undefined; 
      }

      const result = await downloadWorkdayReport(
        employeeId,
        startDate,
        endDate,
        workplaceNameForDownload
      );

      if (result.success) {
        showToast(result.message ?? "Report download initiated successfully!", "success");
        onClose(); 
      } else {
        showToast(result.message ?? "Failed to download report.", "error");
      }
    } catch (error: unknown) {
      console.error("Error during report download:", error);
      showToast("An unexpected error occurred during report download.", "error");
    } finally {
      setIsLoadingReport(false); 
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
              <span className="loading loading-spinner loading-md mx-auto"></span> 
            ) : (
              <select
                name="workplaceId" 
                className="select select-bordered w-full"
                value={selectedWorkplaceId}
                onChange={(e) => setSelectedWorkplaceId(e.target.value)}
              >
                <option value="">All Workplaces</option> 
                {workplaces.map((wp) => (
                  <option key={wp.id} value={wp.id.toString()}>
                    {wp.workplaceName}
                  </option>
                ))}
              </select>
            )}
          </div>
          <div className="modal-action mt-6">
            <button type="submit" className="btn btn-primary" disabled={isLoadingReport || workplaces.length === 0}>
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
