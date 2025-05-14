// app/employees/[id]/page.tsx

import EmployeeDetailClient from "./EmployeeDetailClient";

export default function EmployeeDetailPage({ params }: { params: { id: string } }) {
  return <EmployeeDetailClient id={params.id} />;
}
