package com.narvane.videoservice.util;

import java.io.*;
import java.nio.file.Files;

public class LocalStorageUtil {

    public static File storeTemporarily(InputStream inputStream, String prefix, String suffix) throws IOException {
        File tempFile = Files.createTempFile(prefix, suffix).toFile();

        try (inputStream; FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }

}
