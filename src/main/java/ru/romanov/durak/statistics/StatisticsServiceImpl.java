package ru.romanov.durak.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanov.durak.model.user.User;
import ru.romanov.durak.user.UserRepository;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "statistics", key = "'statistics:' + #page + '-' + #rows + ':' + #sortBy + '-' + #order")
    public StatisticsDto getStatistics(int page, int rows, String sortBy, String order) {
        Page<User> users = userRepository.findAll(page - 1, rows, sortBy, order);
        return new StatisticsDto(users);
    }
}
