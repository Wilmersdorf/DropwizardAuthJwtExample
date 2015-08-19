package brach.stefan.tech.test.rest.model.back;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class AdminUserConnectionDto extends UserConnectionDto {
    private ArrayList<String> connectedTo;

    public ArrayList<String> getConnectedTo() {
        return connectedTo;
    }

    public void setConnectedTo(ArrayList<String> connectedTo) {
        this.connectedTo = connectedTo;
    }

    public void addConnectedTo(String email) {
        if (connectedTo == null) {
            connectedTo = new ArrayList<String>();
        }
        if (!StringUtils.isEmpty(email)) {
            connectedTo.add(email);
        }
    }
}
