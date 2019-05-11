package ru.romanov.durak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.romanov.durak.user.model.User;
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
        model.addAttribute("user", new User());
        model.addAttribute("title", messageHelper.getMessage("registration.title"));
        model.addAttribute("locale", LocaleContextHolder.getLocale());
        return "registration";
    }

    @PostMapping
    public String processRegistration(@ModelAttribute @Valid User user,
                                      BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", messageHelper.getMessage("registration.fail"));
            model.addAttribute("title", messageHelper.getMessage("registration.title"));
            model.addAttribute("user", user);
            return "registration";
        }

        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", messageHelper.getMessage("registration.userExist"));
            model.addAttribute("title", messageHelper.getMessage("registration.title"));
            model.addAttribute("user", user);
            return "registration";
        }

        userService.createAndSaveNewUser(user);

        model.asMap().clear();
        model.addAttribute("title", messageHelper.getMessage("registration.done"));
        model.addAttribute("message", messageHelper.getMessage("registration.done"));
        return "accept";
    }

}
