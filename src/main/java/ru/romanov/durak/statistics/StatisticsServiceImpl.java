package ru.romanov.durak.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanov.durak.model.user.User;
import ru.romanov.durak.user.UserRepository;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "statistics", key = "'statistics:' + #page + '-' + #rows + ':' + #sortBy + '-' + #order")
    public StatisticsDto getStatistics(int page, int rows, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Direction.valueOf(order.toUpperCase()), sortBy);
        PageRequest pageRequest = PageRequest.of(page - 1, rows, sort);
        Page<User> users = userRepository.findAll(pageRequest);
        return new StatisticsDto(users);
    }
}
