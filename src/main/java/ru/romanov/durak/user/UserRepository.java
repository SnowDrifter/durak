package ru.romanov.durak.user;


import org.springframework.data.repository.PagingAndSortingRepository;
import ru.romanov.durak.user.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

    boolean existsUserByUsername(String username);

}
