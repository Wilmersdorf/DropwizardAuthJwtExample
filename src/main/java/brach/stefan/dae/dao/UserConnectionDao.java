package brach.stefan.dae.dao;

import java.util.List;

import brach.stefan.dae.model.UserConnection;

public interface UserConnectionDao {
    void insertConnection(UserConnection userConnection);

    void deleteConnection(UserConnection userConnection);

    List<UserConnection> getAllUserConnectionsByUser(Long userId);

    List<UserConnection> getAllUserConnections();
}
