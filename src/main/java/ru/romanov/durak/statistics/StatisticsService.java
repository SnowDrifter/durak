package ru.romanov.durak.statistics;

public interface StatisticsService {

    StatisticsDto getStatistics(int page, int rows, String sortBy, String order);

}
