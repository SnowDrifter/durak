package ru.romanov.durak.user;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import ru.romanov.durak.model.jooq.tables.records.UserRecord;
import ru.romanov.durak.model.user.User;

import java.util.List;
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
    public Page<User> findAll(int offset, int limit, String sortBy, String order) {
        OrderField<?> orderField;
        if ("asc".equals(order.toLowerCase())) {
            orderField = USER.field(sortBy).asc().nullsFirst();
        } else {
            orderField = USER.field(sortBy).desc().nullsLast();
        }

        List<User> users = context.select()
                .from(USER)
                .orderBy(orderField)
                .limit(limit)
                .offset(offset * limit)
                .fetchInto(User.class);

        long totalCount = findTotalCount();
        PageRequest pageRequest = PageRequest.of(offset, limit);
        return new PageImpl<>(users, pageRequest, totalCount);
    }

    @Override
    public User save(User user) {
        return user.getId() != null ? update(user) : insert(user);
    }

    private User insert(User user) {
        UserRecord record = context.newRecord(USER, user);
        return context.insertInto(USER)
                .set(record)
                .returning(USER.ID)
                .fetchOne()
                .into(User.class);
    }

    private User update(User user) {
        context.newRecord(USER, user).update();
        return user;
    }

    private long findTotalCount() {
        return context.selectCount()
                .from(USER)
                .fetchOneInto(Long.class);
    }
}
