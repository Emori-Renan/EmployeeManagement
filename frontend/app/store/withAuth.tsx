"use client";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useRouter } from "next/navigation";
import { getToken, isTokenValid } from "../utils/auth";
import { loginSuccess } from "./authSlice";
import type { RootState } from "./store"; // update path if needed

const withAuth = <P extends object>(WrappedComponent: React.ComponentType<P>) => {
  const EnhancedComponent = (props: P) => {
    const dispatch = useDispatch();
    const router = useRouter();
    const isAuthenticated = useSelector((state: RootState) => state.auth.isAuthenticated);
    const [checked, setChecked] = useState(false);

    useEffect(() => {
      const token = getToken();

      if (!token || !isTokenValid(token)) {
        router.push(`/login?redirect=${encodeURIComponent(window.location.pathname)}`);
      } else {
        if (!isAuthenticated) {
          dispatch(loginSuccess(token));
        }
        setChecked(true);
      }
    }, [dispatch, isAuthenticated, router]);

    if (!checked) return null;

    return <WrappedComponent {...props} />;
  };

  EnhancedComponent.displayName = `WithAuth(${WrappedComponent.displayName ?? WrappedComponent.name ?? "Component"})`;

  return EnhancedComponent;
};

export default withAuth;