package ru.romanov.durak.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.romanov.durak.model.user.dto.UserView;
import ru.romanov.durak.statistics.StatisticsDto;
import ru.romanov.durak.statistics.StatisticsService;
import ru.romanov.durak.util.MessageHelper;

import static ru.romanov.durak.util.PageConstants.STATISTICS_PAGE;
import static ru.romanov.durak.util.PageConstants.TITLE_ATTRIBUTE;

@Controller
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private MessageHelper messageHelper;

    @GetMapping("/statistic")
    public String statistic(Model model) {
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("statistics.title"));
        return STATISTICS_PAGE;
    }

    @ResponseBody
    @GetMapping("/statistics/data")
    @JsonView(UserView.Statistics.class)
    public StatisticsDto statisticsData(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "20") Integer rows,
                                        @RequestParam(defaultValue = "wins") String sortBy,
                                        @RequestParam(defaultValue = "desc") String order) {
        return statisticsService.getStatistics(page, rows, sortBy, order);
    }

}
