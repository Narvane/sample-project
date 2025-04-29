import "@/app/globals.css";
import Sidebar from "@/components/Sidebar";
import Navbar from "@/components/Navbar";
import React from "react";

export default function RootLayout({ children }: { children: React.ReactNode }) {
    return (
        <html lang="en">
        <body className="flex h-screen overflow-hidden">
        <Sidebar />
        <main className="flex-1 flex flex-col">
            <Navbar />
            <section className="flex-1 overflow-y-auto p-8">{children}</section>
        </main>
        </body>
        </html>
    );
}
