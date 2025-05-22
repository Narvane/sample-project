package com.narvane.videoservice.util;

import java.io.*;
import java.nio.file.Files;

public class VideoUploadUtil {

    public static File saveInputStreamToTempFile(InputStream inputStream, String prefix, String suffix) throws IOException {
        // Cria um arquivo temporário
        File tempFile = Files.createTempFile(prefix, suffix).toFile();

        // Grava o InputStream no arquivo
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            inputStream.close();
        }

        return tempFile;
    }

}
