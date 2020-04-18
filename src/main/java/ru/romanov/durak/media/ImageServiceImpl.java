package ru.romanov.durak.media;

import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final MinioService service;

    private static final String DEFAULT_CONTENT_TYPE = "image";

    @Override
    public byte[] findPhoto(String photoId) throws MinioException, IOException {
        InputStream stream = service.get(Path.of(photoId));
        return stream.readAllBytes();
    }

    @Override
    public String savePhoto(InputStream inputStream) throws MinioException {
        String photoId = generatePhotoId();
        service.upload(Path.of(photoId), inputStream, DEFAULT_CONTENT_TYPE);
        return photoId;
    }

    /**
     * Format: folder/fileName
     * Example: b4e16141/824c45938b9e8682feccc19b
     */
    private String generatePhotoId() {
        return UUID.randomUUID().toString()
                .replaceFirst("-", "/")
                .replaceAll("-", "");
    }
}
