package ru.romanov.durak.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.durak.user.service.UserService;

import java.io.IOException;
import java.util.Base64;

@RestController
public class PhotoController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping("/user/{userId}/photo")
    public ResponseEntity findPhoto(@PathVariable long userId) {
        byte[] photo = userService.findPhoto(userId);

        if (photo != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(photo);
            return ResponseEntity.ok(ImmutableMap.of("photo", photoBase64));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseBody
    @PostMapping("/user/{userId}/photo/upload")
    public ResponseEntity uploadPhoto(@PathVariable long userId, @RequestParam MultipartFile file) throws IOException {
        userService.savePhoto(userId, file.getBytes());
        return ResponseEntity.ok().build();
    }

}
