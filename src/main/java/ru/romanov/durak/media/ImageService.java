package ru.romanov.durak.media;

import com.jlefebure.spring.boot.minio.MinioException;

import java.io.IOException;
import java.io.InputStream;

public interface ImageService {

    byte[] findPhoto(String photoId) throws MinioException, IOException;

    String savePhoto(InputStream inputStream) throws MinioException;

}
