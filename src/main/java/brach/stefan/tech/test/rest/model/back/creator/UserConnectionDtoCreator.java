package brach.stefan.tech.test.rest.model.back.creator;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import brach.stefan.tech.test.model.User;
import brach.stefan.tech.test.rest.model.back.UserConnectionDto;

public class UserConnectionDtoCreator {
    public static List<UserConnectionDto> createUserConnectionDtoList(List<User> users, HashSet<Long> connectedIds) {
        return users.stream().map(user -> {
            UserConnectionDto userConnectionDto = new UserConnectionDto();
            userConnectionDto.setEmail(user.getEmail());
            if (connectedIds.contains(user.getId())) {
                userConnectionDto.setConnected(true);
            }
            return userConnectionDto;
        }).collect(Collectors.toList());
    }
}
