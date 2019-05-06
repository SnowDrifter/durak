package ru.romanov.durak.controller;


import com.google.common.collect.ImmutableMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.romanov.durak.user.UserGrid;
import ru.romanov.durak.user.model.User;
import ru.romanov.durak.user.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Locale;

@Controller
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;

    @GetMapping({"/home", "/"})
    public String home(Model model, Locale locale) {
        model.addAttribute("title", messageSource.getMessage("home.title", null, locale));
        return "home";
    }

    @GetMapping("/singleplayer")
    public String singleplayer(Model model, Locale locale) {
        model.addAttribute("title", messageSource.getMessage("game.singleplayer.title", null, locale));
        return "singleplayer";
    }

    @GetMapping("/multiplayer")
    public String multi(Model model, Locale locale) {
        model.addAttribute("title", messageSource.getMessage("game.multiplayer.title", null, locale));
        return "multiplayer";
    }

    @GetMapping("/rules")
    public String rules(Model model, Locale locale) {
        model.addAttribute("title", messageSource.getMessage("rules.title", null, locale));
        return "rules";
    }

    @GetMapping("/statistic")
    public String statistic(Model model, Locale locale) {
        model.addAttribute("title", messageSource.getMessage("statistics.title", null, locale));
        model.addAttribute("locale", locale);
        return "statistics";
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, Locale locale) {
        ModelAndView model = new ModelAndView("login");
        if (error != null) {
            model.addObject("error", messageSource.getMessage("login.fail", null, locale));
        }
        model.addObject("title", messageSource.getMessage("login.title", null, locale));
        return model;
    }

    @GetMapping("/accessDenied")
    public ModelAndView accessDenied(Principal user, Locale locale) {
        ModelAndView model = new ModelAndView("access-denied");

        if (user != null) {
            model.addObject("error", user.getName() + messageSource.getMessage("accessDenied.withUser", null, locale));
        } else {
            model.addObject("error", messageSource.getMessage("accessDenied.anonymous", null, locale));
        }

        model.addObject("title", messageSource.getMessage("accessDenied.title", null, locale));
        return model;
    }

    @ResponseBody
    @GetMapping("/statistic/data")
    public UserGrid listGrid(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "20") Integer rows,
                             @RequestParam(defaultValue = "wins") String sortBy,
                             @RequestParam(defaultValue = "desc") String order) {
        Sort sort = new Sort(Sort.Direction.valueOf(order.toUpperCase()), sortBy);
        PageRequest pageRequest = new PageRequest(page - 1, rows, sort);
        Page<User> userPage = userService.findAllByPage(pageRequest);
        return new UserGrid(userPage);
    }

    @ResponseBody
    @GetMapping("/profile/{id}")
    public User profile(@PathVariable long id) {
        return userService.findById(id);
    }

    @PostMapping("/edit")
    public String edit(@Valid User user, BindingResult bindingResult, Model model, Locale locale,
                       @RequestParam MultipartFile file) throws IOException {
        logger.info("Updating contact for " + user.getUsername());

        if (file.getSize() > 500000) {
            model.addAttribute("error", messageSource.getMessage("validation.file.size", null, locale));
            model.addAttribute("title", messageSource.getMessage("edit.title", null, locale));
            model.addAttribute("user", user);
            return "edit-profile";
        }

        if (bindingResult.hasErrors()) {
            logger.info("Result has errors: " + bindingResult.getFieldErrorCount());
            model.addAttribute("title", messageSource.getMessage("edit.title", null, locale));
            model.addAttribute("user", user);
            return "edit-profile";
        }

        if (!file.isEmpty()) {
            user.setPhoto(file.getBytes());
        }

        userService.update(user);

        model.asMap().clear();
        model.addAttribute("title", messageSource.getMessage("edit.done", null, locale));
        model.addAttribute("message", messageSource.getMessage("edit.done", null, locale));
        return "accept";
    }

    @GetMapping("/edit")
    public String showEdit(Model model, Locale locale, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("title", messageSource.getMessage("edit.title", null, locale));
        model.addAttribute("locale", locale.toLanguageTag());
        return "edit-profile";
    }

    @ResponseBody
    @GetMapping("/profile/{id}/photo")
    public ResponseEntity findPhotoById(@PathVariable long id) {
        byte[] photo = userService.findPhotoById(id);

        if (photo != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(photo);
            return ResponseEntity.ok(ImmutableMap.of("photo", photoBase64));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}