import React, { useState } from "react";
import { useLoader } from "@/context/LoaderContext";
import {apiFetch} from "@/util/apiFetch";

export default function UploadModal({ isOpen, onClose }: { isOpen: boolean; onClose: () => void }) {
    const [title, setTitle] = useState("");
    const [file, setFile] = useState<File | null>(null);
    const { setLoading } = useLoader();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!file) {
            alert("Please select a video file.");
            return;
        }

        setLoading(true);
        try {
            const formData = new FormData();
            formData.append("title", title);
            formData.append("video", file);

            const res = await apiFetch("/api/videos/upload", {
                method: "POST",
                body: formData,
            });

            if (!res.ok) throw new Error("Upload failed");
            setTitle("");
            setFile(null);
            onClose();
        } catch (err) {
            console.error("Upload error:", err);
            alert("Error uploading video.");
        } finally {
            setLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-40 bg-black bg-opacity-50 flex items-center justify-center">
            <div className="bg-white p-6 rounded-lg w-full max-w-md shadow-lg">
                <h2 className="text-xl font-bold mb-4">Upload Video</h2>
                <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                    <input
                        type="text"
                        placeholder="Video Title"
                        className="border p-2 rounded"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />

                    <input
                        type="file"
                        accept="video/mp4"
                        onChange={(e) => setFile(e.target.files?.[0] || null)}
                        className="border p-2 rounded"
                        required
                    />

                    <div className="flex justify-end gap-2">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-4 py-2 bg-gray-300 rounded"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="px-4 py-2 bg-blue-600 text-white rounded"
                        >
                            Upload
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}