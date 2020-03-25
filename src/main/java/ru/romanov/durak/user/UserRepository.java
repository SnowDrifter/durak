package ru.romanov.durak.user;

import org.springframework.data.domain.Page;
import ru.romanov.durak.model.user.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(long id);

    Optional<User> findByUsername(String username);

    boolean existsUserByUsername(String username);

    byte[] findPhotoById(long id);

    Page<User> findAll(int page, int rows, String sortBy, String order);

    void savePhoto(long id, byte[] photo);

    User save(User user);


}
