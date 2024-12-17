import { useToast } from "../context/ToastContext";

const ToastContainer = () => {
  const { message, type, visible } = useToast();

  return (
    visible && (
      <div
        className={`fixed bottom-4 right-4 z-50 p-4 rounded-md text-white shadow-md
          ${type === 'info' ? 'bg-blue-500' : ''}
          ${type === 'success' ? 'bg-green-500' : ''}
          ${type === 'error' ? 'bg-red-500' : ''}`}
      >
        {message}
      </div>
    )
  );
};

export default ToastContainer;
