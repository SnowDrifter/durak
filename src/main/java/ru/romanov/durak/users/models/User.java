package ru.romanov.durak.users.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@Entity
@Table(name = "\"user\"")
public class User implements UserDetails, Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "{validation.username.notEmpty}")
    @Size(min = 2, max = 25, message = "{validation.username.size}")
    @Pattern(regexp = "[\\d\\w][\\d\\w\\s]*", message = "{validation.username.regexp}")
    private String username;

    @JsonIgnore
    @NotEmpty(message = "{validation.password.notEmpty}")
    @Size(min = 2, max = 64, message = "{validation.password.size}")
    @Column(name = "password", length = 64)
    private String password;

    @Email(message = "{validation.email}")
    private String email;

    @Size(max = 64, message = "{validation.firstname.size}")
    @Pattern(regexp = "[а-яА-Яa-zA-Z]*", message = "{validation.firstname.regexp}")
    private String firstName;

    @Size(max = 64, message = "{validation.lastname.size}")
    @Pattern(regexp = "[а-яА-Яa-zA-Z]*", message = "{validation.lastname.regexp}")
    private String lastName;

    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] photo;

    private String about;
    private Integer wins = 0;
    private Integer loses = 0;
    private Integer totalGames = 0;
    @DateTimeFormat(pattern = "dd MM yyyy")
    private Date birthDate;
    @DateTimeFormat(pattern = "dd MM yyyy")
    private Date creationDate;
    private boolean enabled;

    @Transient
    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    @Transient
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(Role::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    public String getBirthDateString() {
        String birthDateString = "";
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");

        if (birthDate != null) {
            birthDateString = format.format(birthDate);
        }

        return birthDateString;
    }

    @Transient
    public String getCreatingDateString() {
        String creatingDateString = "";
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");

        if (creationDate != null) {
            creatingDateString = format.format(creationDate);
        }

        return creatingDateString;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}

