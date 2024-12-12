import React from "react";

interface ErrorMessageProps {
  errorType: boolean; // Whether the error should be shown
  message: string | null;    // The error message to display
}

const ErrorMessage: React.FC<ErrorMessageProps> = ({ errorType, message }) => {
  if (!errorType) {
    return null; // Do not render anything if errorType is false
  }

  return (
    <div className="label p-0 p-2 pt-0">
      <span className="label-text-alt text-error">{message}</span>
    </div>
  );
};

export default ErrorMessage;
