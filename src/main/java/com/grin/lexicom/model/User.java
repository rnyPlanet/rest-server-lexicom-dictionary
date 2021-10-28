package com.grin.lexicom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grin.lexicom.util.validation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {

    @NotNull
    @NotEmpty
    @ValidUsername
    @UniqueUsername
    @Column(name = "username")
    private String userName;

    @NotNull
    @NotEmpty
    @Size(min=2, max=30)
    @Column(name = "sur_name")
    private String surName;

    @NotNull
    @NotEmpty
    @Size(min=2, max=30)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @NotEmpty
    @Size(min=2, max=30)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @NotEmpty
    @ValidEmail
    @UniqueEmail
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @NotNull
    @NotEmpty
    @ValidPassword
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "id_user", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_role", referencedColumnName = "id")})
    private List<Role> roles;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @NotNull
    @NotEmpty
    @ValidPhone
    @UniquePhone
    @Column(name = "phone")
    private String phone;

    @JsonIgnore
    @ManyToMany(mappedBy = "usersCollection")
    private Collection<Dictionary> subscribedOn;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Dictionary> created;

    public Collection<Dictionary> getSubOn() {
        return subscribedOn;
    }

    @Override
    public String toString() {
        return "userName: " + userName +
                ", surName: " + surName +
                ", firstName: " + firstName +
                ", lastName: " + lastName +
                ", email: " + email +
                ", roles: " + roles +
                ", status: " + status +
                ", phone: " + phone;
    }

}
