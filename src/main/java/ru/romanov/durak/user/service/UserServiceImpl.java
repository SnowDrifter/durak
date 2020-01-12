package ru.romanov.durak.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanov.durak.model.user.UserMapper;
import ru.romanov.durak.model.user.dto.UserDto;
import ru.romanov.durak.user.UserRepository;
import ru.romanov.durak.model.user.Role;
import ru.romanov.durak.model.user.User;

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
    public User update(UserDto userDto) {
        User updatedUser = updateFields(userDto);
        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username);
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
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] findPhoto(long id) {
        return userRepository.findPhotoById(id);
    }

    @Override
    @Transactional
    public void savePhoto(long userId, byte[] photo) {
        userRepository.savePhoto(userId, photo);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void saveNewUser(UserDto userDto) {
        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setCreationDate(new Date());
        user.setEnabled(true);
        userRepository.save(user);
    }

    private User updateFields(UserDto userDto) {
        User user = findByUsername(userDto.getUsername());

        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }

        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getAbout() != null) {
            user.setAbout(userDto.getAbout());
        }

        if (userDto.getBirthDate() != null) {
            user.setBirthDate(userDto.getBirthDate());
        }

        return user;
    }
}

