package ru.romanov.durak.statistics;

import ru.romanov.durak.user.StatisticsDto;

public interface StatisticsService {

    StatisticsDto getStatistics(int page, int rows, String sortBy, String order);

}
