import "@/app/globals.css";
import Sidebar from "@/components/Sidebar";
import Navbar from "@/components/Navbar";
import React from "react";
import SessionWrapper from "@/components/SessionWrapper";
import {getServerSession} from "next-auth";
import {authOptions} from "@/app/api/auth/[...nextauth]/route";
import {redirect} from "next/navigation";

export default async function RootLayout({children}: { children: React.ReactNode }) {
    const session = await getServerSession(authOptions);

    if (!session) {
        redirect("/api/auth/signin");
    }

    return (
        <html lang="en">
        <body className="flex h-screen overflow-hidden">
        <SessionWrapper>
            <Sidebar session={session}/>
            <main className="flex-1 flex flex-col">
                <Navbar/>
                <section className="flex-1 overflow-y-auto p-8">{children}</section>
            </main>
        </SessionWrapper>
        </body>
        </html>
    );
}
