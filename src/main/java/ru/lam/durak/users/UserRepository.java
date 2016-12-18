package ru.lam.durak.users;


import org.springframework.data.repository.PagingAndSortingRepository;
import ru.lam.durak.users.models.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

}
