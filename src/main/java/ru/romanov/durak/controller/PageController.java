package ru.romanov.durak.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.romanov.durak.util.MessageHelper;

import static ru.romanov.durak.util.PageConstants.*;

@Slf4j
@Controller
public class PageController {

    @Autowired
    private MessageHelper messageHelper;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("home.title"));
        return HOME_PAGE;
    }

    @GetMapping("/singleplayer")
    public String singleplayer(Model model) {
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("game.singleplayer.title"));
        return SINGLEPLAYER_PAGE;
    }

    @GetMapping("/multiplayer")
    public String multiplayer(Model model) {
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("game.multiplayer.title"));
        return MULTIPLAYER_PAGE;
    }

    @GetMapping("/rules")
    public String rules(Model model) {
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("rules.title"));
        return RULES_PAGE;
    }

}