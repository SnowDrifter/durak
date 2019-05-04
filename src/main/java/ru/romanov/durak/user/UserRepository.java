package ru.romanov.durak.user;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.romanov.durak.user.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

    boolean existsUserByUsername(String username);

    @Query("SELECT u.photo FROM User u where u.id = :id")
    byte[] findPhotoById(@Param("id") Long id);

}
