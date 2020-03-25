package ru.romanov.durak.user;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import ru.romanov.durak.model.user.User;

import java.util.Optional;

import static ru.romanov.durak.model.jooq.tables.User.USER;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext context;

    @Override
    public Optional<User> findById(long id) {
        User user = context.select()
                .from(USER)
                .where(USER.ID.eq(id))
                .fetchOne()
                .into(User.class);

        return Optional.of(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        User user = context.select()
                .from(USER)
                .where(USER.USERNAME.eq(username))
                .fetchOne()
                .into(User.class);

        return Optional.of(user);
    }

    @Override
    public boolean existsUserByUsername(String username) {
        return context.fetchExists(
                context.select()
                        .from(USER)
                        .where(USER.USERNAME.eq(username))
        );
    }

    @Override
    public byte[] findPhotoById(long id) {
        // TODO: implement
        return new byte[0];
    }

    @Override
    public Page<User> findAll(int page, int rows, String sortBy, String order) {
        // TODO: implement
        return null;
    }

    @Override
    public void savePhoto(long id, byte[] photo) {
        // TODO: implement
    }

    @Override
    public User save(User user) {
        // TODO: implement
        return null;
    }
}
