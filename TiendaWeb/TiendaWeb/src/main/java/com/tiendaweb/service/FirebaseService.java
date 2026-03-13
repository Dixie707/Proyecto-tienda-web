package com.tiendaweb.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FirebaseService {

    public String subirImagen(MultipartFile imagen) throws IOException {
        String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();

        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(nombreArchivo, imagen.getBytes(), imagen.getContentType());

        return "https://firebasestorage.googleapis.com/v0/b/"
                + bucket.getName()
                + "/o/"
                + URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8)
                + "?alt=media";
    }
}