package ru.romanov.durak.user;


import lombok.Data;
import org.springframework.data.domain.Page;
import ru.romanov.durak.model.user.User;

import java.util.List;

@Data
public class StatisticsDto {

    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<User> usersData;

    public StatisticsDto(Page<User> userPage) {
        this.totalPages = userPage.getTotalPages();
        this.currentPage = userPage.getNumber() + 1;
        this.totalRecords = userPage.getTotalElements();
        this.usersData = userPage.getContent();
    }
}
