package ru.romanov.durak.user.service;


import com.jlefebure.spring.boot.minio.MinioException;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.romanov.durak.model.user.dto.UserDto;
import ru.romanov.durak.model.user.User;

import java.io.InputStream;

public interface UserService extends UserDetailsService {

    User save(User user);

    User update(UserDto userDto);

    User findById(Long id);

    User findByUsername(String username);

    void updatePhoto(long id, InputStream stream) throws MinioException;

    void saveNewUser(UserDto user);

    boolean existsByUsername(String username);

}
