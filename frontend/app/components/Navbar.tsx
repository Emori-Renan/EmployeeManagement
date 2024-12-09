"use client"; 


const Navbar = () => {
    return (
      <div className="navbar bg-orange-50 shadow-sm mb-3">
        <link rel="icon" href="/favicon.png" />
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
          <a className="btn btn-ghost text-xl">My Application</a>
        </div>
        <div className="flex-none">
          <button className="btn btn-square btn-ghost">
          <input type="checkbox" value="synthwave" className="toggle theme-controller" />
          </button>
        </div>
      </div>
    );
  };
  
  export default Navbar;
  