package ru.romanov.durak.users.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;


@Entity
@Table(name="app_user")
@JsonAutoDetect
public class User implements UserDetails, Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> roles = new HashSet<>();
    private String about;
    private byte[] photo;
    private Integer wins = 0;
    private Integer loses = 0;
    private Integer totalGames = 0;
    private Date birthDate;
    private Date creationDate;
    private boolean enabled;
    private int version;

    @Version
    @Column(name = "version")
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }

    @Transient
    @Override
    public boolean isNew() {
        return id == null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnore
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @NotEmpty(message="{validation.username.notEmpty}")
    @Size(min=2, max=25, message="{validation.username.size}")
    @Pattern(regexp="[\\d\\w][\\d\\w\\s]*", message="{validation.username.regexp}")
    @Column(name = "username", length = 25)
    @Type(type = "string")
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    @NotEmpty(message="{validation.password.notEmpty}")
    @Size(min=2, max=64, message="{validation.password.size}")
    @Column(name = "password", length = 64)
    @Type(type = "string")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password=password;
    }

    @JsonIgnore
    @Override
    @Column(name = "enabled")
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    @Column(name = "email")
    @Email(message="{validation.email}")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "first_name", length = 64)
    @Size(max=64, message="{validation.firstname.size}")
    @Pattern(regexp="[а-яА-Яa-zA-Z]*", message="{validation.firstname.regexp}")
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", length = 64)
    @Size(max=64, message="{validation.lastname.size}")
    @Pattern(regexp="[а-яА-Яa-zA-Z]*", message="{validation.lastname.regexp}")
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @DateTimeFormat(pattern = "dd MM yyyy")
    public Date getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @DateTimeFormat(pattern = "dd MM yyyy")
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAbout() {
        return about;
    }
    public void setAbout(String about) {
        this.about = about;
    }

    @Basic(fetch= FetchType.LAZY)
    @Lob
    @Column(name = "photo")
    public byte[] getPhoto() {
        return photo;
    }
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Column(name = "wins", length = 5)
    public Integer getWins() {
        return wins;
    }
    public void setWins(Integer wins) {
        this.wins = wins;
    }

    @Column(name = "loses", length = 5)
    public Integer getLoses() {
        return loses;
    }
    public void setLoses(Integer loses) {
        this.loses = loses;
    }

    @Column(name = "total_games", length = 5)
    public Integer getTotalGames() {
        return totalGames;
    }
    public void setTotalGames(Integer totalGames) {
        this.totalGames = totalGames;
    }

    @JsonIgnore
    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> result = new ArrayList<>();

        for (Role role : getRoles()) {
            result.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        return result;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    public String getBirthDateString() {
        String birthDateString = "";
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");

        if (birthDate != null){
            birthDateString = format.format(birthDate);
        }

        return birthDateString;
    }

    @Transient
    public String getCreatingDateString() {
        String creatingDateString = "";
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");

        if (creationDate != null){
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

