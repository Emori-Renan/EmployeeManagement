'use client';
import React, { useState, useEffect } from "react";
import {
  getWorkdaysByEmployeeAndFilters,
  fetchWorkplacesByEmployeeId,
} from "@/app/controller/WorkdayController";

interface WorkdayViewerModalProps {
  readonly isOpen: boolean;
  readonly onClose: () => void;
  readonly employeeId: number;
}

interface WorkdayDisplayData {
  id: number;
  date: string;
  workplaceName: string;
  hoursWorked: number;
  overtimeHours: number;
  transportCost: number;
}

interface WorkplaceOption {
  id: number;
  workplaceName: string;
}

export default function WorkdayViewerModal({
  isOpen,
  onClose,
  employeeId,
}: WorkdayViewerModalProps) {
  const [workdays, setWorkdays] = useState<WorkdayDisplayData[]>([]);
  const [workplaces, setWorkplaces] = useState<WorkplaceOption[]>([]);
  const [filters, setFilters] = useState({
    startDate: '',
    endDate: '',
    workplaceId: '',
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingWorkplaces, setIsLoadingWorkplaces] = useState(false);

  const formatDate = (date: Date) => date.toISOString().split('T')[0];

  useEffect(() => {
    if (!isOpen) return;

    const today = new Date();
    const defaultStartDate = formatDate(new Date(today.getFullYear(), today.getMonth(), 1));
    const defaultEndDate = formatDate(new Date(today.getFullYear(), today.getMonth() + 1, 0));

    setFilters((prev) => ({
      ...prev,
      startDate: prev.startDate || defaultStartDate,
      endDate: prev.endDate || defaultEndDate,
    }));

    const loadWorkplaces = async () => {
      setIsLoadingWorkplaces(true);
      setError('');
      const response = await fetchWorkplacesByEmployeeId(employeeId);
      if (response.success && response.data) {
        setWorkplaces(response.data);
      } else {
        setError(response.message || "Failed to load workplaces.");
      }
      setIsLoadingWorkplaces(false);
    };

    loadWorkplaces();
  }, [isOpen, employeeId]);

  const fetchWorkdays = async () => {
    setError('');
    setIsLoading(true);

    const response = await getWorkdaysByEmployeeAndFilters(employeeId, {
      startDate: filters.startDate || undefined,
      endDate: filters.endDate || undefined,
      workplaceId: filters.workplaceId ? Number(filters.workplaceId) : undefined,
    });

    if (response.success && response.data) {
      setWorkdays(response.data);
    } else {
      setWorkdays([]);
      setError(response.message || "No workdays found.");
    }

    setIsLoading(false);
  };

  const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const totalHoursWorked = workdays.reduce((sum, w) => sum + w.hoursWorked, 0);
  const totalOvertime = workdays.reduce((sum, w) => sum + w.overtimeHours, 0);
  const totalTransport = workdays.reduce((sum, w) => sum + w.transportCost, 0);

  if (!isOpen) return null;

  return (
    <dialog id="workday_viewer_modal" className="modal modal-open">
      <div className="modal-box w-11/12 max-w-5xl">
        <h3 className="text-xl font-bold mb-4">View Workdays</h3>

        <div className="flex flex-wrap gap-4 mb-4 items-end">
          <div className="form-control w-full md:w-1/4">
            <label className="label">
              <span className="label-text">Start Date</span>
            </label>
            <input
              type="date"
              name="startDate"
              className="input input-bordered w-full"
              value={filters.startDate}
              onChange={handleFilterChange}
            />
          </div>

          <div className="form-control w-full md:w-1/4">
            <label className="label">
              <span className="label-text">End Date</span>
            </label>
            <input
              type="date"
              name="endDate"
              className="input input-bordered w-full"
              value={filters.endDate}
              onChange={handleFilterChange}
            />
          </div>

          <div className="form-control w-full md:w-1/4">
            <label className="label">
              <span className="label-text">Workplace</span>
            </label>
            {isLoadingWorkplaces ? (
              <span className="loading loading-spinner"></span>
            ) : (
              <select
                name="workplaceId"
                className="select select-bordered w-full"
                value={filters.workplaceId}
                onChange={handleFilterChange}
                disabled={workplaces.length === 0} 
              >
                <option value="" >
                  {workplaces.length === 0 ? "No workplaces available" : "All Workplaces"}
                </option>
                {workplaces.map((wp) => (
                  <option key={wp.id} value={wp.id}>
                    {wp.workplaceName}
                  </option>
                ))}
              </select>
            )}
          </div>

          <div className="form-control w-full md:w-auto mt-6 md:mt-0">
            <button
              className="btn btn-primary"
              onClick={fetchWorkdays}
              disabled={isLoading || workplaces.length === 0}
            >
              {isLoading ? "Searching..." : "Search Workdays"}
            </button>
          </div>
        </div>

        {error && <div className="text-red-500 mb-4">{error}</div>}

        {workdays.length > 0 ? (
          <div className="overflow-x-auto">
            <table className="table table-zebra w-full">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Workplace</th>
                  <th>Hours</th>
                  <th>Overtime</th>
                  <th>Transport</th>
                </tr>
              </thead>
              <tbody>
                {workdays.map((wd, index) => (
                  <tr key={wd.id ?? index}>
                    <td>{wd.date}</td>
                    <td>{wd.workplaceName}</td>
                    <td>{wd.hoursWorked.toFixed(2)}</td>
                    <td>{wd.overtimeHours.toFixed(2)}</td>
                    <td>{wd.transportCost.toFixed(2)}</td>
                  </tr>
                ))}
                <tr className="font-bold">
                  <td colSpan={2}>Total:</td>
                  <td>{totalHoursWorked.toFixed(2)}</td>
                  <td>{totalOvertime.toFixed(2)}</td>
                  <td>{totalTransport.toFixed(2)}</td>
                </tr>
              </tbody>
            </table>
          </div>
        ) : (
          !isLoading && (
            <p className="text-center mt-4 text-gray-500">
              No workdays found. Try different filters.
            </p>
          )
        )}

        <div className="modal-action">
          <button className="btn btn-ghost" onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </dialog>
  );
}
