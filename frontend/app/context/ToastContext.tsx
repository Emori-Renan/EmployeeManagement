import React, { createContext, useContext, useState, ReactNode } from "react";

type ToastContextType = {
  showToast: (message: string, type: "info" | "success" | "error") => void;
  message: string | null;
  type: "info" | "success" | "error" | null;
  visible: boolean;
};

type ToastProviderProps = {
  children: ReactNode;
};

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export const useToast = (): ToastContextType => {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error("useToast must be used within a ToastProvider");
  }
  return context;
};

export const ToastProvider: React.FC<ToastProviderProps> = ({ children }) => {
  const [message, setMessage] = useState<string | null>(null);
  const [type, setType] = useState<"info" | "success" | "error" | null>(null);
  const [visible, setVisible] = useState(false);

  const showToast = (message: string, type: "info" | "success" | "error") => {
    setMessage(message);
    setType(type);
    setVisible(true);

    setTimeout(() => {
      setVisible(false);
    }, 3000); 
  };

  return (
    <ToastContext.Provider value={{ showToast, message, type, visible }}>
      {children}
    </ToastContext.Provider>
  );
};
