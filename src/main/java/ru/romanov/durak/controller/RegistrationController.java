package ru.romanov.durak.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.romanov.durak.model.user.dto.UserDto;
import ru.romanov.durak.user.service.UserService;
import ru.romanov.durak.util.MessageHelper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static ru.romanov.durak.util.PageConstants.*;

@Slf4j
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageHelper messageHelper;

    @GetMapping
    public String showRegistration(Model model) {
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("registration.title"));
        model.addAttribute(USER_ATTRIBUTE, new UserDto());
        return REGISTRATION_PAGE;
    }

    @PostMapping
    public String processRegistration(HttpServletRequest request, @Valid UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("registration.title"));
            model.addAttribute(USER_ATTRIBUTE, userDto);
            model.addAttribute(ERROR_ATTRIBUTE, messageHelper.getMessage("registration.fail"));
            return REGISTRATION_PAGE;
        }

        if (userService.existsByUsername(userDto.getUsername())) {
            model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("registration.title"));
            model.addAttribute(USER_ATTRIBUTE, userDto);
            model.addAttribute(ERROR_ATTRIBUTE, messageHelper.getMessage("registration.userExist"));
            return REGISTRATION_PAGE;
        }

        userService.saveNewUser(userDto);

        model.asMap().clear();
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("registration.done"));
        model.addAttribute(MESSAGE_ATTRIBUTE, messageHelper.getMessage("registration.done"));

        try {
            request.login(userDto.getUsername(), userDto.getPassword());
        } catch (Exception e) {
            log.error("Auto-login error. {}: {}", e.getClass().getSimpleName(), e.getMessage());
        }

        return SUCCESS_PAGE;
    }

}
