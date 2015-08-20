package brach.stefan.tech.test.rest.model.back;

import java.util.ArrayList;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

public class AdminUserConnectionDto {
    private String email;
    private ArrayList<String> connectedToEmails = new ArrayList<String>();

    public AdminUserConnectionDto() {
    }

    public AdminUserConnectionDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getConnectedToEmails() {
        return connectedToEmails;
    }

    public void setConnectedToEmails(ArrayList<String> connectedToEmails) {
        this.connectedToEmails = connectedToEmails;
    }

    public void addConnectedToEmail(@Nullable String email) {
        if (connectedToEmails == null) {
            connectedToEmails = new ArrayList<String>();
        }
        if (!StringUtils.isBlank(email)) {
            connectedToEmails.add(email);
        }
    }
}
