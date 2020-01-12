package ru.romanov.durak.model.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "{validation.username.notEmpty}")
    @Size(min = 2, max = 25, message = "{validation.username.size}")
    @Pattern(regexp = "[\\d\\w][\\d\\w\\s]*", message = "{validation.username.regexp}")
    private String username;

    @JsonIgnore
    @NotBlank(message = "{validation.password.notEmpty}")
    @Size(min = 2, max = 64, message = "{validation.password.size}")
    private String password;

    @Email(message = "{validation.email}")
    private String email;

    @Size(max = 64, message = "{validation.firstname.size}")
    @Pattern(regexp = "[а-яА-Яa-zA-Z]*", message = "{validation.firstname.regexp}")
    private String firstName;

    @Size(max = 64, message = "{validation.lastname.size}")
    @Pattern(regexp = "[а-яА-Яa-zA-Z]*", message = "{validation.lastname.regexp}")
    private String lastName;

    private String about;
    private Integer wins = 0;
    private Integer loses = 0;
    private Integer totalGames = 0;
    @DateTimeFormat(pattern = "dd MM yyyy")
    private Date birthDate;
    @DateTimeFormat(pattern = "dd MM yyyy")
    private Date creationDate;
    private boolean enabled;

}
