// ToastContainer.tsx
import { useToast } from "../context/ToastContext";

const ToastContainer = () => {
  const { message, type, visible } = useToast();
  
  if (!visible || !message || !type) return null; // Do not render if no message
  const alertClass = `alert alert-${type}`;
  return (
    <div className="toast toast-end">
      <div className={alertClass}>
        <span>{message}</span>
      </div>
    </div>
  );
};

export default ToastContainer;
