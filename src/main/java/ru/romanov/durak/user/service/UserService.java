package ru.romanov.durak.user.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.romanov.durak.user.model.User;

public interface UserService extends UserDetailsService {

    User save(User user);

    User update(User user);

    Page<User> findAllByPage(Pageable pageable);

    User findById(Long id);

    User findByUsername(String username);

    byte[] findPhotoById(long id);

    void createAndSaveNewUser(User user);

    boolean existsByUsername(String username);

}
