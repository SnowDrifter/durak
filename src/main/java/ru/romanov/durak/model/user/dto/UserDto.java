package ru.romanov.durak.model.user.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class UserDto {

    @JsonView(UserView.Full.class)
    private Long id;

    @JsonView(UserView.Statistics.class)
    @NotBlank(message = "{validation.username.notEmpty}")
    @Size(min = 2, max = 25, message = "{validation.username.size}")
    @Pattern(regexp = "[\\d\\w][\\d\\w\\s]*", message = "{validation.username.regexp}")
    private String username;

    @NotBlank(message = "{validation.password.notEmpty}")
    @Size(min = 2, max = 64, message = "{validation.password.size}")
    private String password;

    @JsonView(UserView.Full.class)
    @Email(message = "{validation.email}")
    private String email;

    @JsonView(UserView.Full.class)
    @Size(max = 64, message = "{validation.firstname.size}")
    @Pattern(regexp = "[а-яА-Яa-zA-Z]*", message = "{validation.firstname.regexp}")
    private String firstName;

    @JsonView(UserView.Full.class)
    @Size(max = 64, message = "{validation.lastname.size}")
    @Pattern(regexp = "[а-яА-Яa-zA-Z]*", message = "{validation.lastname.regexp}")
    private String lastName;

    @JsonView(UserView.Full.class)
    private String about;

    @JsonView(UserView.Statistics.class)
    private Integer wins;

    @JsonView(UserView.Statistics.class)
    private Integer loses;

    @JsonView(UserView.Statistics.class)
    private Integer totalGames;

    @JsonView(UserView.Full.class)
    @DateTimeFormat(pattern = "dd MM yyyy")
    private Date birthDate;

    @JsonView(UserView.Full.class)
    @DateTimeFormat(pattern = "dd MM yyyy")
    private Date creationDate;

    @JsonView(UserView.Full.class)
    private boolean enabled;

    @JsonView(UserView.Full.class)
    private boolean hasPhoto;

}
