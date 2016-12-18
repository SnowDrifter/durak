package ru.lam.durak.users.service;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.lam.durak.users.UserRepository;
import ru.lam.durak.users.models.Role;
import ru.lam.durak.users.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class); // TODO Add logs
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User update(User user) {
        User updatedUser = updateFields(user);
        return entityManager.merge(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
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
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void createAndSaveNewUser(User user) {
        Set<Role> baseRoles = new HashSet<>();

        baseRoles.add(Role.ROLE_USER);
        user.setRoles(baseRoles);

        String rawPassword = user.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder(11);
        user.setPassword(encoder.encode(rawPassword));

        user.setCreationDate(new Date());
        user.setEnabled(true);

        userRepository.save(user);
    }

    private User updateFields(User user){
        User oldUser = userRepository.findByUsername(user.getUsername());

        if(user.getFirstName() != null){
            oldUser.setFirstName(user.getFirstName());
        }

        if(user.getLastName() != null){
            oldUser.setLastName(user.getLastName());
        }

        if(user.getEmail() != null){
            oldUser.setEmail(user.getEmail());
        }

        if(user.getAbout() != null){
            oldUser.setAbout(user.getAbout());
        }

        if(user.getPhoto() != null){
            oldUser.setPhoto(user.getPhoto());
        }

        if(user.getBirthDate() != null){
            oldUser.setBirthDate(user.getBirthDate());
        }

        return oldUser;
    }
}

