package ru.lam.durak.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.lam.durak.users.UserGrid;
import ru.lam.durak.users.models.User;
import ru.lam.durak.users.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.StringWriter;
import java.security.Principal;
import java.util.Locale;

@Controller
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
    public String home(Model model, Locale locale) {
        model.addAttribute("title", messageSource.getMessage("home.title", null, locale));
        return "home";
    }

    @RequestMapping(value = "/singleplayer", method = RequestMethod.GET)
    public String single(Model model, Locale locale) {
        model.addAttribute("title", messageSource.getMessage("game.singleplayer.title", null, locale));
        return "singleplayer";
    }

    @RequestMapping(value = "/multiplayer", method = RequestMethod.GET)
    public String multi(Model model, Locale locale) {
        model.addAttribute("title", messageSource.getMessage("game.multiplayer.title", null, locale));
        return "multiplayer";
    }

    @RequestMapping(value = "/rules", method = RequestMethod.GET)
    public String rules(Model model, Locale locale) {
        model.addAttribute("title", messageSource.getMessage("rules.title", null, locale));
        return "rules";
    }

    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    public String statistics(Model model, Locale locale) {
        model.addAttribute("title", messageSource.getMessage("statistics.title", null, locale));
        model.addAttribute("locale", locale);
        return "statistics";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error, Locale locale) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", messageSource.getMessage("login.fail", null, locale));
        }
        model.addObject("title", messageSource.getMessage("login.title", null, locale));
        model.setViewName("login");
        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("remember-me")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        return "redirect:/home";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public ModelAndView accessDenied(Principal user, Locale locale) {
        ModelAndView model = new ModelAndView();

        if (user != null) {
            model.addObject("error", user.getName() + messageSource.getMessage("accessDenied.withUser", null, locale));
        } else {
            model.addObject("error", messageSource.getMessage("accessDenied.anonymous", null, locale));
        }

        model.addObject("title", messageSource.getMessage("accessDenied.title", null, locale));
        model.setViewName("access-denied");
        return model;
    }

    @RequestMapping(value = "/stat/listgrid", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public UserGrid listGrid(@RequestParam(value = "page", required = false) Integer page,
                             @RequestParam(value = "rows", required = false) Integer rows,
                             @RequestParam(value = "sidx", required = false) String sortBy,
                             @RequestParam(value = "sord", required = false) String order) {
        Sort sort = null;
        String orderBy = sortBy;
        PageRequest pageRequest;

        if (orderBy != null && orderBy.equals("wins")) {
            orderBy = "wins";
        }

        if (orderBy != null && order != null) {
            if (order.equals("desc")) {
                sort = new Sort(Sort.Direction.DESC, orderBy);
            } else {
                sort = new Sort(Sort.Direction.ASC, orderBy);
            }
        }

        if (sort != null) {
            pageRequest = new PageRequest(page - 1, rows, sort);
        } else {
            pageRequest = new PageRequest(page - 1, rows);
        }

        Page<User> userPage = userService.findAllByPage(pageRequest);

        UserGrid usersGrid = new UserGrid();
        usersGrid.setCurrentPage(userPage.getNumber() + 1);
        usersGrid.setTotalPages(userPage.getTotalPages());
        usersGrid.setTotalRecords(userPage.getTotalElements());
        usersGrid.setUsersData(Lists.newArrayList(userPage.iterator()));

        return usersGrid;
    }

    @ResponseBody
    @RequestMapping(value = "/profile/{username}", method = RequestMethod.GET)
    public String profile(@PathVariable("username") String username) {
        User user = userService.findByUsername(username);

        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(writer, user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@Valid User user, BindingResult bindingResult, Model model, Locale locale,
                       @RequestParam("file") MultipartFile file) throws IOException {
        logger.info("Updating contact for " + user.getUsername());

        if (file.getSize() > 500000) {
            model.addAttribute("error", messageSource.getMessage("validation.file.size", null, locale));
            model.addAttribute("title", messageSource.getMessage("edit.title", null, locale));
            model.addAttribute("user", user);
            return "edit-profile";
        }

        if (bindingResult.hasErrors()) {
            logger.info("Result has errors:");
            for (FieldError error : bindingResult.getFieldErrors()) {
                logger.info(error.toString());
            }

            model.addAttribute("title", messageSource.getMessage("edit.title", null, locale));
            model.addAttribute("user", user);
            return "edit-profile";
        }

        model.asMap().clear();

        if (!file.isEmpty()) {
            user.setPhoto(file.getBytes());
        }

        userService.update(user);

        model.addAttribute("title", messageSource.getMessage("edit.done", null, locale));
        model.addAttribute("message", messageSource.getMessage("edit.done", null, locale));
        return "accept";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String showEdit(Model model, Locale locale, Principal principal) {

        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("title", messageSource.getMessage("edit.title", null, locale));
        model.addAttribute("locale", locale.toLanguageTag());

        return "edit-profile";
    }

    @RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public byte[] downloadPhoto(@PathVariable("id") Long id) {
        User user = userService.findById(id);

        return user.getPhoto();
    }

}