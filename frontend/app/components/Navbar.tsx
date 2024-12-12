"use client";

import Link from "next/link";

const Navbar = () => {
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
      <div className="flex-none">
      <a href="/register" className="btn btn-ghost text-m">Sign up</a>
      </div>
      <div className="flex-none">
      <a href="/login" className="btn btn-ghost text-m">Sign in</a>
      </div>
      <div className="flex-none">
      <a className="btn btn-ghost text-m">Logout</a>
      </div>
    </div>
  );
};

export default Navbar;
