package ru.lam.durak.users.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.lam.durak.users.models.User;

public interface UserService {
    User save(User user);
    User update(User user);
    Page<User> findAllByPage(Pageable pageable);
    User findById(Long id);
    User findByUsername(String username);
    void createAndSaveNewUser(User user);
}
