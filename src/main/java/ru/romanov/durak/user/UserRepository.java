package ru.romanov.durak.user;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.romanov.durak.model.user.User;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsUserByUsername(String username);

    @Query("SELECT u.photo FROM User u where u.id = :id")
    byte[] findPhotoById(@Param("id") long id);

    @Modifying
    @Query("UPDATE User u SET u.photo = :photo where u.id = :id")
    void savePhoto(@Param("id") long id, @Param("photo") byte[] photo);

}
