// src/components/Sidebar.tsx
"use client";
import Link from "next/link";
import {usePathname} from "next/navigation";
import {Session} from "next-auth";
import {signOut} from "next-auth/react";

const routes = [
    { href: "/",        label: "Home" }
];

export default function Sidebar({ session }: { session: Session }) {
    const pathname = usePathname();

    return (
        <aside className="w-56 bg-sidebar border-r flex flex-col">
            <div className="px-6 py-8 text-xl font-bold">Home</div>

            <nav className="flex-1">
                {routes.map(r => (
                    <Link
                        key={r.href}
                        href={r.href}
                        className={`block px-6 py-3 hover:bg-gray-200 ${
                            pathname === r.href ? "bg-gray-200 font-semibold" : ""
                        }`}
                    >
                        {r.label}
                    </Link>
                ))}
            </nav>

            <div className="mt-auto px-6 py-4 border-t">
                <p className="text-sm mb-2">{session.user?.name}</p>
                <button className="text-left text-sm text-primary hover:underline" onClick={() => signOut()}>
                    Logout
                </button>
            </div>
        </aside>
    );
}
