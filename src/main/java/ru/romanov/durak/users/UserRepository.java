package ru.romanov.durak.users;


import org.springframework.data.repository.PagingAndSortingRepository;
import ru.romanov.durak.users.models.User;
import ru.romanov.durak.users.models.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

}
