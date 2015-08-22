package brach.stefan.dae.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import brach.stefan.dae.model.Role;
import brach.stefan.dae.model.User;

public class UserMapper implements ResultSetMapper<User> {
    public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        User user = new User();
        user.setEmail(r.getString("email"));
        user.setHash(r.getString("hash"));
        user.setId(r.getLong("id"));
        user.setRole(Role.valueOf(r.getString("role")));
        return user;
    }
}