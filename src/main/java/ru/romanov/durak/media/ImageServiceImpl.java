package ru.romanov.durak.media;

import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public byte[] findPhoto(long id) {
        return new byte[0];
    }

    @Override
    public void savePhoto(long id, byte[] photo) {

    }
}
