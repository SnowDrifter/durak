package ru.romanov.durak.controller;

import com.google.common.collect.ImmutableMap;
import com.jlefebure.spring.boot.minio.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.durak.media.ImageService;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
public class PhotoController {

    private final ImageService imageService;

    @GetMapping("/media/photo")
    public ResponseEntity findPhoto(@RequestParam String photoId) throws MinioException, IOException {
        byte[] photo = imageService.findPhoto(photoId);

        if (photo != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(photo);
            return ResponseEntity.ok(ImmutableMap.of("photo", photoBase64));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
