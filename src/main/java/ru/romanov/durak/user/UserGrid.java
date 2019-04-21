package ru.romanov.durak.user;


import lombok.Data;
import ru.romanov.durak.user.model.User;

import java.util.List;

@Data
public class UserGrid {

    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<User> usersData;

}
