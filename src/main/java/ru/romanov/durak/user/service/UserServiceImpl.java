package ru.romanov.durak.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanov.durak.user.UserRepository;
import ru.romanov.durak.user.model.Role;
import ru.romanov.durak.user.model.User;

import java.util.Collections;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User update(User user) {
        User updatedUser = updateFields(user);
        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllByPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] findPhotoById(long id) {
        return userRepository.findPhotoById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void createAndSaveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setCreationDate(new Date());
        user.setEnabled(true);
        userRepository.save(user);
    }

    private User updateFields(User user) {
        User oldUser = userRepository.findByUsername(user.getUsername());

        if (user.getFirstName() != null) {
            oldUser.setFirstName(user.getFirstName());
        }

        if (user.getLastName() != null) {
            oldUser.setLastName(user.getLastName());
        }

        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }

        if (user.getAbout() != null) {
            oldUser.setAbout(user.getAbout());
        }

        if (user.getPhoto() != null) {
            oldUser.setPhoto(user.getPhoto());
        }

        if (user.getBirthDate() != null) {
            oldUser.setBirthDate(user.getBirthDate());
        }

        return oldUser;
    }
}

