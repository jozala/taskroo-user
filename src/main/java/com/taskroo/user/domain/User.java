package com.taskroo.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Collections;
import java.util.Set;

public class User {

    @NotBlank
    private final String _id;
    @NotBlank
    private final String firstName;
    @NotBlank
    private final String lastName;
    @Email
    private final String email;
    @NotBlank
    private final String password;
    private final Set<Role> roles;
    private final boolean enabled;
    private final String salt;

    @JsonCreator
    public static User createNewUser(@JsonProperty("username") String username, @JsonProperty("email") String email,
                                     @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName,
                                     @JsonProperty("password") String password) {
        return new User(username, email, firstName, lastName, password, Collections.singleton(Role.USER), true, null);
    }

    public User(String username, String email, String firstName, String lastName, String password, Set<Role> roles,
                 boolean enabled, String salt) {
        this._id = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.roles = Collections.unmodifiableSet(roles);
        this.enabled = enabled;
        this.salt = salt;
    }

    public String get_id() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getSalt() {
        return salt;
    }

}
