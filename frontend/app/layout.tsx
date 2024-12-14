"use client";
import { ReactNode } from "react";
import Navbar from "./components/Navbar";
import Drawer from "./components/Drawer";
import "./globals.css";
import { Provider } from 'react-redux';
import store from './store/store';

export default function RootLayout({
  children,
}: {
  children: ReactNode;
}) {
  return (
    <Provider store={store}>

      <html lang="en" data-theme="cupcake">
        <body>
          <link rel="icon" href="/favicon.png" />
          <header className="navbar sticky top-0 z-50 p-0">
            <Navbar />
          </header>

          <main>
            <div className="drawer">
              <input id="my-drawer" type="checkbox" className="drawer-toggle" />
              <div className="drawer-content flex">
                {children}
              </div>
              <Drawer />
            </div>
          </main>

          <footer className="flex items-center justify-center bottom-0 fixed h-12 w-full">
            <p className="text-sm text-gray-400">© 2024 My Application</p>
          </footer>
        </body>
      </html>
    </Provider>

  );
}