"use client";
import { ReactNode, useEffect } from "react";
import Navbar from "./components/Navbar";
import Drawer from "./components/Drawer";
import "./globals.css";
import { Provider, useDispatch } from "react-redux";
import store from "./store/store";
import { ToastProvider } from "./context/ToastContext";
import ToastContainer from "./components/ToastContainer";
import { setToken } from "./store/authSlice";

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

  useEffect(() => {
    // Only run this code on the client side
    const token = localStorage.getItem("token");
    if (token) {
      dispatch(setToken(token)); // Set token in Redux store
    }
  }, [dispatch]);

  return (
    <html lang="en" data-theme="cupcake">
      <body>
        <link rel="icon" href="/favicon.png" />
        <header className="navbar sticky top-0 z-50 p-0">
          <Navbar />
        </header>

        <main>
          <div className="drawer">
            <input id="my-drawer" type="checkbox" className="drawer-toggle" />
            <div className="drawer-content flex">{children}</div>
            <Drawer />
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
