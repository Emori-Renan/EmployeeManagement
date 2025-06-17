import EmployeeDetailClient from "./EmployeeDetailClient";

export async function generateStaticParams() {
  return [
    { id: '1' },
    { id: '2' },
    { id: '3' },
  ];
}

export default function Page() {
  return <EmployeeDetailClient />;
}
