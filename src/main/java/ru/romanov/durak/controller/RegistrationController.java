package ru.romanov.durak.controller;

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

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageHelper messageHelper;

    @GetMapping
    public String showRegistration(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("title", messageHelper.getMessage("registration.title"));
        return "registration";
    }

    @PostMapping
    public String processRegistration(@Valid UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", messageHelper.getMessage("registration.fail"));
            model.addAttribute("title", messageHelper.getMessage("registration.title"));
            model.addAttribute("userDto", userDto);
            return "registration";
        }

        if (userService.existsByUsername(userDto.getUsername())) {
            model.addAttribute("error", messageHelper.getMessage("registration.userExist"));
            model.addAttribute("title", messageHelper.getMessage("registration.title"));
            model.addAttribute("userDto", userDto);
            return "registration";
        }

        userService.saveNewUser(userDto);

        model.asMap().clear();
        model.addAttribute("title", messageHelper.getMessage("registration.done"));
        model.addAttribute("message", messageHelper.getMessage("registration.done"));
        return "accept";
    }

}
