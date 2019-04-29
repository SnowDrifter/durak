package ru.romanov.durak.user;


import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.data.domain.Page;
import ru.romanov.durak.user.model.User;

import java.util.List;

@Data
public class UserGrid {

    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<User> usersData;

    public UserGrid(Page<User> userPage) {
        this.totalPages = userPage.getTotalPages();
        this.currentPage = userPage.getNumber() + 1;
        this.totalRecords = userPage.getTotalElements();
        this.usersData = Lists.newArrayList(userPage.iterator());
    }
}
