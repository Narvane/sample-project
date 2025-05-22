import { getSession } from "next-auth/react";

export async function apiFetch(path: string, init: RequestInit = {}) {
    const session = await getSession();

    if (!session || !session.accessToken) {
        throw new Error("No access token found. Are you logged in?");
    }

    const headers = new Headers(init.headers || {});
    headers.set("Authorization", `Bearer ${session.accessToken}`);

    const fullUrl = path.startsWith("http")
        ? path
        : `${process.env.NEXT_PUBLIC_API_BASE_URL}${path.startsWith("/") ? "" : "/"}${path}`;

    return fetch(fullUrl, {
        ...init,
        headers,
    });
}