package ru.lam.durak.controllers;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import ru.lam.durak.users.models.User;
import ru.lam.durak.users.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class RegistrationControllerTest {

    private final List<User> users = new ArrayList<>();

    @Before
    public void initUser(){
        User user = new User();
        user.setUsername("username");
        user.setPassword("1234");
        user.setId(1L);
        users.add(user);
    }

    // TODO
    @Test
    public void testProcessRegistration(){
        User user = new User();
        user.setId(2L);
        user.setUsername("qwerty");
        user.setPassword("1234");
        user.setFirstName("John");
        user.setLastName("Doe");

        UserService service = Mockito.mock(UserService.class);

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                users.add(user);
                return null;
            }
        }).when(service).createAndSaveNewUser(user);

    }

}
