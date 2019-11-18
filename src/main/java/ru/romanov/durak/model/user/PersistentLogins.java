package ru.romanov.durak.model.user;

import lombok.Data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "persistent_logins")
public class PersistentLogins {

    @NotNull
    private String username;
    @Id
    private String series;
    @NotNull
    private String token;
    @NotNull
    @Column(name = "last_used")
    private Date lastUsed;

}