package ru.romanov.durak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.romanov.durak.user.model.User;
import ru.romanov.durak.user.service.UserService;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(method = RequestMethod.GET)
    public String showRegistration(Model model, Locale locale) {
        model.addAttribute("user", new User());
        model.addAttribute("title", messageSource.getMessage("registration.title", null, locale));
        model.addAttribute("locale", locale.toLanguageTag());
        return "registration";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processRegistration(@ModelAttribute @Valid User user,
                                      BindingResult result, Model model, Locale locale) {
        if (result.hasErrors()) {
            model.addAttribute("error", messageSource.getMessage("registration.fail", null, locale));
            model.addAttribute("title", messageSource.getMessage("registration.title", null, locale));
            model.addAttribute("user", user);
            return "registration";
        }

        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", messageSource.getMessage("registration.userExist", null, locale));
            model.addAttribute("title", messageSource.getMessage("registration.title", null, locale));
            model.addAttribute("user", user);
            return "registration";
        }

        userService.createAndSaveNewUser(user);

        model.asMap().clear();
        model.addAttribute("title", messageSource.getMessage("registration.done", null, locale));
        model.addAttribute("message", messageSource.getMessage("registration.done", null, locale));
        return "accept";
    }

}
