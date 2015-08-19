package brach.stefan.tech.test.rest.model.send;

public class ConnectToUserDto {

    private String email;
    private boolean connected;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

}
