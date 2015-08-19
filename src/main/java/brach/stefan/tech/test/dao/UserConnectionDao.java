package brach.stefan.tech.test.dao;

import java.util.List;

import brach.stefan.tech.test.model.UserConnection;

public interface UserConnectionDao {
    void insertConnection(UserConnection userConnection);

    void deleteConnection(UserConnection userConnection);

    List<UserConnection> getAllUserConnectionsByUser(Long userId);

    List<UserConnection> getAllUserConnections(Long userId);
}
