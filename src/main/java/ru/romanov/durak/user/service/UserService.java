package ru.romanov.durak.user.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.romanov.durak.model.user.dto.UserDto;
import ru.romanov.durak.model.user.User;

public interface UserService extends UserDetailsService {

    User save(User user);

    User update(UserDto userDto);

    Page<User> findAllByPage(Pageable pageable);

    User findById(Long id);

    User findByUsername(String username);

    byte[] findPhoto(long userId);

    void savePhoto(long userId, byte[] photo);

    void saveNewUser(UserDto user);

    boolean existsByUsername(String username);

}
