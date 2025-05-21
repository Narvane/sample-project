"use client";
import React, { createContext, useContext, useState } from "react";

const LoaderContext = createContext({
    loading: false,
    setLoading: (_: boolean) => {},
});

export const useLoader = () => useContext(LoaderContext);

export const LoaderProvider = ({ children }: { children: React.ReactNode }) => {
    const [loading, setLoading] = useState(false);

    return (
        <LoaderContext.Provider value={{ loading, setLoading }}>
            {loading && (
                <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center">
                    <div className="text-white text-lg">Loading...</div>
                </div>
            )}
            {children}
        </LoaderContext.Provider>
    );
};