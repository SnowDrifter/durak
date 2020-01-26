package ru.romanov.durak.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.romanov.durak.model.user.UserMapper;
import ru.romanov.durak.model.user.dto.UserDto;
import ru.romanov.durak.model.user.dto.UserView;
import ru.romanov.durak.statistics.StatisticsService;
import ru.romanov.durak.statistics.StatisticsDto;
import ru.romanov.durak.user.service.UserService;
import ru.romanov.durak.util.MessageHelper;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

import static ru.romanov.durak.util.PageConstants.*;

@Slf4j
@Controller
public class MainController {

    @Autowired
    private UserService userService;
    @Autowired
    private StatisticsService statisticsService;
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

    @GetMapping("/statistic")
    public String statistic(Model model) {
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("statistics.title"));
        return STATISTICS_PAGE;
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error) {
        ModelAndView model = new ModelAndView(LOGIN_PAGE);
        model.addObject(TITLE_ATTRIBUTE, messageHelper.getMessage("login.title"));
        
        if (error != null) {
            model.addObject(ERROR_ATTRIBUTE, messageHelper.getMessage("login.fail"));
        }
        return model;
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

    @ResponseBody
    @GetMapping("/profile/{id}")
    public UserDto profile(@PathVariable long id) {
        return UserMapper.INSTANCE.userToUserDto(userService.findById(id));
    }

    @PostMapping("/edit")
    @JsonView(UserView.Full.class)
    public String edit(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Edit result has errors. Count: {}", bindingResult.getErrorCount());
            model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("edit.title"));
            model.addAttribute(USER_ATTRIBUTE, userDto);
            return EDIT_PROFILE_PAGE;
        }

        userService.update(userDto);

        model.asMap().clear();
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("edit.done"));
        model.addAttribute(MESSAGE_ATTRIBUTE, messageHelper.getMessage("edit.done"));
        return SUCCESS_PAGE;
    }

    @GetMapping("/edit")
    @JsonView(UserView.Full.class)
    public String showEdit(Model model, Principal principal) {
        UserDto userDto = UserMapper.INSTANCE.userToUserDto(userService.findByUsername(principal.getName()));
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("edit.title"));
        model.addAttribute(USER_ATTRIBUTE, userDto);
        return EDIT_PROFILE_PAGE;
    }

    @ResponseBody
    @GetMapping("/profile/{id}/photo")
    public ResponseEntity findPhotoById(@PathVariable long id) {
        byte[] photo = userService.findPhoto(id);

        if (photo != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(photo);
            return ResponseEntity.ok(ImmutableMap.of("photo", photoBase64));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseBody
    @PostMapping("/profile/{id}/photo/upload")
    public ResponseEntity uploadPhoto(@PathVariable long id, @RequestParam MultipartFile file) throws IOException {
        userService.savePhoto(id, file.getBytes());
        return ResponseEntity.ok().build();
    }

}