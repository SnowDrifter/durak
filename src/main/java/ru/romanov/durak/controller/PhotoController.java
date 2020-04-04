package ru.romanov.durak.controller;

import com.jlefebure.spring.boot.minio.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.durak.media.ImageService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PhotoController {

    private final ImageService imageService;

    @GetMapping(value = "/media/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> findPhoto(@RequestParam String photoId) throws MinioException, IOException {
        byte[] photo = imageService.findPhoto(photoId);

        if (photo != null) {
            return ResponseEntity.ok(photo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
