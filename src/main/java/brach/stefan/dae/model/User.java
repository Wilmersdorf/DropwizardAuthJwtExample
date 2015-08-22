package brach.stefan.dae.model;

import java.security.Principal;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User implements Principal {
    private String email;
    private String hash;
    private Long id;
    private Role role = Role.NORMAL;

    public User() {
    }

    public User(String email, String hash) {
        this.email = email;
        this.hash = hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*
     * dropwizard requires to implement principal for authentication which
     * implements getName()
     */
    @JsonIgnore
    @Override
    public String getName() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
