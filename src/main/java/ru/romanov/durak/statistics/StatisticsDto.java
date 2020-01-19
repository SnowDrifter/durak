package ru.romanov.durak.statistics;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.data.domain.Page;
import ru.romanov.durak.model.user.User;
import ru.romanov.durak.model.user.UserMapper;
import ru.romanov.durak.model.user.dto.UserDto;
import ru.romanov.durak.model.user.dto.UserView;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonView(UserView.Statistics.class)
public class StatisticsDto {

    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<UserDto> usersData;

    public StatisticsDto(Page<User> userPage) {
        this.totalPages = userPage.getTotalPages();
        this.currentPage = userPage.getNumber() + 1;
        this.totalRecords = userPage.getTotalElements();

        this.usersData = userPage.getContent().stream()
                .map(UserMapper.INSTANCE::userToUserDto)
                .collect(Collectors.toList());
    }
}
