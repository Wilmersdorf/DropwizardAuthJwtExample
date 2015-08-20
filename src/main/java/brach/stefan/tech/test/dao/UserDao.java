package brach.stefan.tech.test.dao;

import java.util.List;

import brach.stefan.tech.test.model.User;

public interface UserDao {
    public void createUser(User user) throws Exception;

    public User findUserByEmail(String email);

    public List<User> getAllNormalUsers();

    public List<User> getAllNormalUsersExcept(Long userId);
}
