package ru.romanov.durak.users;


import lombok.Data;
import ru.romanov.durak.users.models.User;

import java.util.List;

@Data
public class UserGrid {

    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<User> usersData;

}
