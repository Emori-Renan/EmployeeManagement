"use client";
import { useDispatch, useSelector } from "react-redux";
import { logout as logoutAction } from "../store/authSlice";
import { useRouter } from 'next/navigation'
import Link from "next/link";
import { clearToken } from "../utils/auth";
import LoadingModal from "./Loading";
import { useState } from "react";
import { delay } from "../utils/functions";
import { RootState } from "../store/store";
import { useToast } from "../context/ToastContext";

const Navbar = () => {
  const router = useRouter();
  const dispatch = useDispatch();
  const [isLoading, setIsLoading] = useState(false)

  const isAuthenticated = useSelector((state: RootState) => state.auth.isAuthenticated)
  
  const { showToast } = useToast();
  
  const logout = async () => {
    clearToken();
    setIsLoading(true)
    await delay(2000);
    setIsLoading(false);
    dispatch(logoutAction());
    showToast("User logout successfully!", "success");
    router.push("/")
  };

  return (
    <div className="navbar bg-orange-50 shadow-sm">

      <div className="flex-none">
        <label htmlFor="my-drawer" className="btn drawer-button">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            className="inline-block h-5 w-5 stroke-current"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M4 6h16M4 12h16M4 18h16"
            ></path>
          </svg>
        </label>
      </div>
      <div className="flex-1 pl-3">
        <Link href="/" className="btn btn-ghost text-xl">My Application</Link>
      </div>
      {!isAuthenticated ? (<><div className="flex-none">
        <Link href="/register" className="btn btn-ghost text-m">Sign up</Link>
      </div><div className="flex-none">
          <Link href="/login" className="btn btn-ghost text-m">Sign in</Link>
        </div></>) : (<div className="flex-none">
        <button onClick={logout} className="btn btn-ghost text-m">Logout</button>
      </div>)}
      <LoadingModal isLoading={isLoading} message="Loggin out..." />
      
    </div>
  );
};

export default Navbar;