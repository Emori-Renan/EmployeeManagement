"use client";

import React, { FormEvent } from "react";
import { useToast } from "../context/ToastContext";
// Define the Workplace type locally if not available from a module
type Workplace = {
  workplaceName: string;
  hourlyWage: number;
  overtimeMultiplier: number;
  employeeId: number;
};

interface Props {
  isOpen: boolean;
  onClose: () => void;
  onRegister: (workplace: Workplace) => void
  ;
  employeeId: number;
}

export default function WorkplaceRegistrationModal({
  isOpen,
  onClose,
  onRegister,
  employeeId,
}: Props) {
  const { showToast } = useToast();

  if (!isOpen) return null;

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const workplaceName = formData.get("name") as string;
    const hourlyWageStr = formData.get("hourlyWage") as string;
    const overtimeMultiplierStr = formData.get("overtimeMultiplier") as string;

    if (workplaceName && hourlyWageStr && overtimeMultiplierStr) {
      const hourlyWage = parseFloat(hourlyWageStr);
      const overtimeMultiplier = parseFloat(overtimeMultiplierStr);

      if (!isNaN(hourlyWage) && !isNaN(overtimeMultiplier) && hourlyWage > 0 && overtimeMultiplier > 0) {
        onRegister({ workplaceName, hourlyWage, overtimeMultiplier, employeeId });
      } else {  
        showToast("Please enter valid numeric values for Hourly Wage and Overtime Multiplier.", "error");
      }
    } else {
      showToast("Please fill in all fields.", "error");
    }
  };

  return (
    <dialog id="workplace_modal" className="modal modal-middle modal-open">
      <div className="modal-box">
        <h3 className="font-bold text-lg">Register Workplace</h3>
        <form onSubmit={handleSubmit}>
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
            <button type="button" className="btn btn-ghost" onClick={onClose}>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </dialog>
  );
}
