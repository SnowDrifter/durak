package ru.romanov.durak.model.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@Entity
@Table(name = "\"user\"")
@ToString(of = "username")
@EqualsAndHashCode(of = "username")
public class User implements UserDetails, Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    @Column(length = 64)
    private String password;

    private String email;

    private String firstName;

    private String lastName;

    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Lob
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;

    private String about;
    @Column(columnDefinition = "int4 default 0")
    private int wins;
    @Column(columnDefinition = "int4 default 0")
    private int loses;
    @Column(columnDefinition = "int4 default 0")
    private int totalGames;
    @DateTimeFormat(pattern = "dd MM yyyy")
    private Date birthDate;
    @DateTimeFormat(pattern = "dd MM yyyy")
    private Date creationDate;
    private boolean enabled;

    @Override
    @Transient
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

    @JsonProperty
    public boolean hasPhoto() {
        return photo != null;
    }

}

