// app/employees/[id]/EmployeeDetailClient.tsx
"use client";

import { useEffect, useState } from "react";

export default function EmployeeDetailClient({ id }: { id: string }) {
  const [employee, setEmployee] = useState<any>(null);
  const [error, setError] = useState("");

  useEffect(() => {
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
        if (!res.ok) {
          const data = await res.json();
          setError(data.message || "Error fetching employee");
        } else {
          const data = await res.json();
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
      <p><strong>Role:</strong> {employee.role}</p>
    </div>
  );
}
