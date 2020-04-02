package ru.romanov.durak.media;

public interface ImageService {

    byte[] findPhoto(long id);

    void savePhoto(long id, byte[] photo);

}
