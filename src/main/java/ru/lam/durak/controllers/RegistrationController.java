package ru.lam.durak.controllers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.lam.durak.users.models.User;
import ru.lam.durak.users.service.UserService;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private static final Logger logger = LogManager.getLogger(RegistrationController.class); // TODO: add logs

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
    public String processRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult result, Model model, Locale locale) {
        if (result.hasErrors()) {
            model.addAttribute("error", messageSource.getMessage("registration.fail", null, locale));
            model.addAttribute("title", messageSource.getMessage("registration.title", null, locale));
            model.addAttribute("user", user);
            return "registration";
        }

        User registeredUser = userService.findByUsername(user.getUsername());

        if(registeredUser != null){
            model.addAttribute("error", messageSource.getMessage("registration.userExist", null, locale));
            model.addAttribute("title", messageSource.getMessage("registration.title", null, locale));
            model.addAttribute("user", user);
            return "registration";
        }

        model.asMap().clear();

        userService.createAndSaveNewUser(user);

        model.addAttribute("title", messageSource.getMessage("registration.done", null, locale));
        model.addAttribute("message", messageSource.getMessage("registration.done", null, locale));
        return "accept";
    }

}
