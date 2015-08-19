package brach.stefan.tech.test.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import brach.stefan.tech.test.exception.typed.IdConflictException;
import brach.stefan.tech.test.model.UserConnection;

public class UserConnectionMapper implements ResultSetMapper<UserConnection> {
    public UserConnection map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        long lowerId = r.getLong("lowerId");
        long higherId = r.getLong("higherId");
        UserConnection userConnection = null;
        try {
            userConnection = new UserConnection(lowerId, higherId);
        } catch (IdConflictException e) {
            throw new RuntimeException("UserConnection map failed.");
        }
        return userConnection;
    }
}