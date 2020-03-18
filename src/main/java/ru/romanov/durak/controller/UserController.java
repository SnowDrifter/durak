package ru.romanov.durak.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.romanov.durak.model.user.UserMapper;
import ru.romanov.durak.model.user.dto.UserDto;
import ru.romanov.durak.model.user.dto.UserView;
import ru.romanov.durak.user.service.UserService;
import ru.romanov.durak.util.MessageHelper;

import javax.validation.Valid;
import java.security.Principal;

import static ru.romanov.durak.util.PageConstants.*;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageHelper messageHelper;

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
    @GetMapping("/user/{id}")
    public UserDto getUser(@PathVariable long id) {
        return UserMapper.INSTANCE.userToUserDto(userService.findById(id));
    }

    @GetMapping("/edit")
    @JsonView(UserView.Full.class)
    public String showEdit(Model model, Principal principal) {
        UserDto userDto = UserMapper.INSTANCE.userToUserDto(userService.findByUsername(principal.getName()));
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("edit.title"));
        model.addAttribute(USER_ATTRIBUTE, userDto);
        return EDIT_PROFILE_PAGE;
    }

    @PostMapping("/edit")
    @JsonView(UserView.Full.class)
    public String editProfile(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Edit result has {} errors", bindingResult.getErrorCount());
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

}
