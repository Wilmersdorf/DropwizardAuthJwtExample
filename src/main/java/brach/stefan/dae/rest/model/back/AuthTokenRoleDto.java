package brach.stefan.dae.rest.model.back;

import brach.stefan.dae.model.Role;

public class AuthTokenRoleDto {
    private String authToken;
    private Role role = Role.NORMAL;

    public AuthTokenRoleDto() {
    }

    public AuthTokenRoleDto(String authToken, Role role) {
        this.authToken = authToken;
        this.role = role;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
