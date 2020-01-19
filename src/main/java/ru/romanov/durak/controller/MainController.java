package ru.romanov.durak.controller;


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
import ru.romanov.durak.statistics.StatisticsService;
import ru.romanov.durak.user.StatisticsDto;
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
        model.addAttribute("title", messageHelper.getMessage("home.title"));
        return HOME_PAGE;
    }

    @GetMapping("/singleplayer")
    public String singleplayer(Model model) {
        model.addAttribute("title", messageHelper.getMessage("game.singleplayer.title"));
        return SINGLEPLAYER_PAGE;
    }

    @GetMapping("/multiplayer")
    public String multiplayer(Model model) {
        model.addAttribute("title", messageHelper.getMessage("game.multiplayer.title"));
        return MULTIPLAYER_PAGE;
    }

    @GetMapping("/rules")
    public String rules(Model model) {
        model.addAttribute("title", messageHelper.getMessage("rules.title"));
        return RULES_PAGE;
    }

    @GetMapping("/statistic")
    public String statistic(Model model) {
        model.addAttribute("title", messageHelper.getMessage("statistics.title"));
        return STATISTICS_PAGE;
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error) {
        ModelAndView model = new ModelAndView(LOGIN_PAGE);
        if (error != null) {
            model.addObject("error", messageHelper.getMessage("login.fail"));
        }
        model.addObject("title", messageHelper.getMessage("login.title"));
        return model;
    }

    @ResponseBody
    @GetMapping("/statistics/data")
    public StatisticsDto statisticsData(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "20") Integer rows,
                                        @RequestParam(defaultValue = "wins") String sortBy,
                                        @RequestParam(defaultValue = "desc") String order) {
        return statisticsService.getStatistics(page, rows, sortBy, order);
    }

    @ResponseBody
    @GetMapping("/profile/{id}")
    public User profile(@PathVariable long id) {
        return userService.findById(id);
    }

    @PostMapping("/edit")
    public String edit(@Valid UserDto userDto, BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            log.debug("Result has errors: " + bindingResult.getFieldErrorCount());
            model.addAttribute("title", messageHelper.getMessage("edit.title"));
            model.addAttribute("userDto", userDto);
            return EDIT_PROFILE_PAGE;
        }

        userService.update(userDto);

        model.asMap().clear();
        model.addAttribute("title", messageHelper.getMessage("edit.done"));
        model.addAttribute("message", messageHelper.getMessage("edit.done"));
        return SUCCESS_PAGE;
    }

    @GetMapping("/edit")
    public String showEdit(Model model, Principal principal) {
        model.addAttribute("userDto", userService.findByUsername(principal.getName()));
        model.addAttribute("title", messageHelper.getMessage("edit.title"));
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