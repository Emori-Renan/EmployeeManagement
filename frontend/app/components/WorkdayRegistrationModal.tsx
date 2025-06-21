import React, { useState, useEffect } from "react";
import { fetchWorkplacesByEmployeeId, workdayRegistration } from "../controller/WorkdayController";
import { useToast } from "../context/ToastContext";

interface WorkdayRegistrationModalProps {
  readonly isOpen: boolean;
  readonly onClose: () => void;
  readonly employeeId: number;
  readonly onWorkdayRegistered: () => void;
}

interface WorkplaceOption {
  id: number;
  workplaceName: string;
}

export default function WorkdayRegistrationModal({
  isOpen,
  onClose,
  employeeId,
  onWorkdayRegistered,
}: WorkdayRegistrationModalProps) {
  const [workplaceId, setWorkplaceId] = useState<string>("");
  const [date, setDate] = useState<string>(new Date().toISOString().split('T')[0]);
  const [hoursWorked, setHoursWorked] = useState<string>("");
  const [overtimeHours, setOvertimeHours] = useState<string>("");
  const [transportCost, setTransportCost] = useState<string>("");
  const [workplaces, setWorkplaces] = useState<WorkplaceOption[]>([]);
  const [error, setError] = useState<string>("");
  const [isLoadingWorkplaces, setIsLoadingWorkplaces] = useState<boolean>(false);
  const { showToast } = useToast();

  useEffect(() => {
    const loadWorkplaces = async () => {
      if (!isOpen || !employeeId) return;

      setIsLoadingWorkplaces(true);
      setError(""); 
      const response = await fetchWorkplacesByEmployeeId(employeeId);

      if (response.success && response.data) {
        setWorkplaces(response.data);
        if (response.data.length > 0) {
          setWorkplaceId(String(response.data[0].id)); 
        } else {
          setError("No workplaces found for this employee. Please register a workplace first.");
          setWorkplaceId(""); 
        }
      } else {
        setError(response.message ?? "Failed to load workplaces.");
      }
      setIsLoadingWorkplaces(false);
    };

    loadWorkplaces();
  }, [isOpen, employeeId]);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError("");

    const parsedHoursWorked = parseFloat(hoursWorked);
    const parsedOvertimeHours = parseFloat(overtimeHours);
    const parsedTransportCost = parseFloat(transportCost);

    if (
      !workplaceId ||
      !date ||
      isNaN(parsedHoursWorked) ||
      isNaN(parsedOvertimeHours) ||
      isNaN(parsedTransportCost) ||
      parsedHoursWorked < 0 || parsedOvertimeHours < 0 || parsedTransportCost < 0
    ) {
      setError("Please fill in all fields with valid numbers.");
      return;
    }
    if (parsedHoursWorked === 0 && parsedOvertimeHours === 0) {
      setError("Hours Worked or Overtime Hours must be greater than 0.");
      return;
    }


    const workdayData = {
      employeeId: employeeId,
      workplaceId: Number(workplaceId),
      date: date,
      hoursWorked: parsedHoursWorked,
      overtimeHours: parsedOvertimeHours,
      transportCost: parsedTransportCost,
    };

    const response = await workdayRegistration(workdayData);
    if (response.success) {
      showToast("Workday registered successfully!", "success");
      onWorkdayRegistered(); 
      onClose(); 

      setWorkplaceId(workplaces.length > 0 ? String(workplaces[0].id) : "");
      setDate(new Date().toISOString().split('T')[0]);
      setHoursWorked("");
      setOvertimeHours("");
      setTransportCost("");
    } else {
      setError(response.message ?? "Error registering workday.");
    }
  };

  if (!isOpen) return null;

  return (
    <dialog id="workday_modal" className="modal modal-middle modal-open">
      <div className="modal-box">
        <h3 className="font-bold text-lg">Register Workday</h3>
        {error && <div className="text-red-500 mb-4">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-control">
            <label className="label">
              <span className="label-text">Workplace</span>
            </label>
            {isLoadingWorkplaces ? (
              <span className="loading loading-spinner loading-md"></span>
            ) : (
              <select
                className="select select-bordered w-full"
                value={workplaceId}
                onChange={(e) => setWorkplaceId(e.target.value)}
                required
                disabled={workplaces.length === 0} 
              >
                <option value="" disabled>
                  {workplaces.length === 0 ? "No workplaces available" : "Select a workplace"}
                </option>
                {workplaces.map((wp) => (
                  <option key={wp.id} value={wp.id}>
                    {wp.workplaceName}
                  </option>
                ))}
              </select>
            )}
          </div>

          <div className="form-control mt-4">
            <label className="label">
              <span className="label-text">Date</span>
            </label>
            <input
              type="date"
              className="input input-bordered"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              required
            />
          </div>

          <div className="form-control mt-4">
            <label className="label">
              <span className="label-text">Hours Worked</span>
            </label>
            <input
              type="number"
              placeholder="e.g., 8"
              className="input input-bordered"
              value={hoursWorked}
              onChange={(e) => setHoursWorked(e.target.value)}
              step="0.01"
              min="0" 
              required
            />
          </div>

          <div className="form-control mt-4">
            <label className="label">
              <span className="label-text">Overtime Hours</span>
            </label>
            <input
              type="number"
              placeholder="e.g., 2"
              className="input input-bordered"
              value={overtimeHours}
              onChange={(e) => setOvertimeHours(e.target.value)}
              step="0.01"
              min="0" 
              required
            />
          </div>

          <div className="form-control mt-4">
            <label className="label">
              <span className="label-text">Transport Cost</span>
            </label>
            <input
              type="number"
              placeholder="e.g., 15.50"
              className="input input-bordered"
              value={transportCost}
              onChange={(e) => setTransportCost(e.target.value)}
              step="0.01"
              min="0" 
              required
            />
          </div>

          <div className="modal-action mt-6">
            <button type="submit" className="btn btn-primary"
              disabled={isLoadingWorkplaces || workplaces.length === 0}>
              Register
            </button>
            <button type="button" className="btn btn-ghost" onClick={onClose}>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </dialog>
  );
}