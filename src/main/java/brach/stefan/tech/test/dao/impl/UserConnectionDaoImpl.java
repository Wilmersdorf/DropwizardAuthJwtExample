package brach.stefan.tech.test.dao.impl;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import brach.stefan.tech.test.LogSqlFactory;
import brach.stefan.tech.test.dao.UserConnectionDao;
import brach.stefan.tech.test.dao.mapper.UserConnectionMapper;
import brach.stefan.tech.test.model.UserConnection;

@LogSqlFactory
@RegisterMapper(UserConnectionMapper.class)
public interface UserConnectionDaoImpl extends UserConnectionDao {
    @Override
    @SqlUpdate("REPLACE INTO connections (lowerId, higherId) values (:lowerId, :higherId)")
    void insertConnection(@BindBean UserConnection userConnection);

    @Override
    @SqlUpdate("DELETE FROM connections WHERE lowerId =:lowerId AND higherId =:higherId")
    void deleteConnection(@BindBean UserConnection userConnection);

    @Override
    @SqlQuery("SELECT * FROM connections WHERE lowerId =:userId OR higherId =:userId")
    List<UserConnection> getAllUserConnectionsByUser(@Bind("userId") Long userId);

    @Override
    @SqlQuery("SELECT * FROM connections")
    List<UserConnection> getAllUserConnections();
}
