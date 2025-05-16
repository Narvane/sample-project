"use client";

import PostModal from "@/components/UploadModal";
import {useState} from "react";


export default function Navbar() {
    const [showModal, setShowModal] = useState(false);

    return (
        <>
            <header className="h-16 flex items-center justify-end px-8 border-b">
                <button className="px-4 py-2 rounded-md border shadow-sm" onClick={() => setShowModal(true)}>
                    Upload
                </button>
            </header>
            <PostModal isOpen={showModal} onClose={() => setShowModal(false)} />
        </>
    );
}
