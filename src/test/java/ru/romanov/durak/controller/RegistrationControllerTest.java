package ru.romanov.durak.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import ru.romanov.durak.model.user.dto.UserDto;
import ru.romanov.durak.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class RegistrationControllerTest {

    private final List<UserDto> users = new ArrayList<>();

    @Before
    public void initUser(){
        UserDto user = new UserDto();
        user.setUsername("username");
        user.setPassword("1234");
        user.setId(1L);
        users.add(user);
    }

    @Test
    public void testProcessRegistration(){
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setUsername("qwerty");
        userDto.setPassword("1234");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");

        UserService service = Mockito.mock(UserService.class);

        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            users.add(userDto);
            return null;
        }).when(service).saveNewUser(userDto);
    }

}
