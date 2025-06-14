import React from "react";

interface ErrorMessageProps {
  errorType: boolean;
  message: string | null;   
}

const ErrorMessage: React.FC<ErrorMessageProps> = ({ errorType, message }) => {
  if (!errorType) {
    return null; 
  }

  return (
    <div className="label p-0 p-2 pt-0">
      <span className="label-text-alt text-error">{message}</span>
    </div>
  );
};

export default ErrorMessage;
