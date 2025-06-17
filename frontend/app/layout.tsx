"use client";
import { ReactNode, useEffect, useState } from "react";
import Navbar from "./components/Navbar";
import Drawer from "./components/Drawer";
import "./globals.css";
import { Provider, useDispatch } from "react-redux";
import store from "./store/store";
import { ToastProvider, useToast } from "./context/ToastContext";
import ToastContainer from "./components/ToastContainer";
import { logout, setToken, setUsername } from "./store/authSlice";
import { getToken, getUsernameFromToken, isTokenValid } from "./utils/auth";

const RootLayout = ({ children }: { children: ReactNode }) => {
  return (
    <Provider store={store}>
      <ToastProvider>
        <MainLayout>{children}</MainLayout>
      </ToastProvider>
    </Provider>
  );
};

const MainLayout = ({ children }: { children: ReactNode }) => {
  const dispatch = useDispatch();
  const { showToast } = useToast();
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);

  const closeDrawer = () => {
    setIsDrawerOpen(false);
  };

  useEffect(() => {
    const username = getUsernameFromToken();
    if (username) {
      dispatch(setUsername(username)); 
    }
  }, [dispatch, showToast]);

  useEffect(() => {
    const token = getToken();
    if (token != null) {
      if (isTokenValid(token)) {
        dispatch(setToken(token)); 
      } else {
        
        localStorage.removeItem("token"); 
        dispatch(logout()); 
        showToast("Session expired. Log in again.", "info");
      }
    } 
  }, [dispatch, showToast]);

  return (
    <html lang="en" data-theme="cupcake">
      <body>
        <link rel="icon" href="/favicon.png" />
        <header className="navbar sticky top-0 z-50 p-0">
          <Navbar closeDrawer={closeDrawer} />
        </header>

        <main>
          <div className="drawer">
            <input id="my-drawer" type="checkbox" className="drawer-toggle"
              onChange={() => setIsDrawerOpen(!isDrawerOpen)} checked={isDrawerOpen}/>
            <div className="drawer-content flex">{children}</div>
            <Drawer closeDrawer={closeDrawer} />
          </div>
        </main>

        <footer className="flex items-center justify-center bottom-0 fixed h-12 w-full">
          <p className="text-sm text-gray-400">© 2024 My Application</p>
        </footer>

        <ToastContainer />
      </body>
    </html>
  );
};

export default RootLayout;
