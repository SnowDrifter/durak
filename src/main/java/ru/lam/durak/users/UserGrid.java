package ru.lam.durak.users;


import ru.lam.durak.users.models.User;

import java.util.List;

public class UserGrid {
    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<User> usersData;

    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalRecords() {
        return totalRecords;
    }
    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<User> getUsersData() {
        return usersData;
    }
    public void setUsersData(List<User> usersData) {
        this.usersData = usersData;
    }


}
