package brach.stefan.tech.test.model.extractor;

import java.util.HashSet;
import java.util.List;

import brach.stefan.tech.test.exception.typed.NoSuchIdException;
import brach.stefan.tech.test.model.UserConnection;

public class UserConnectionListExtractor {
    public static HashSet<Long> extractOtherUserIds(List<UserConnection> userConnections, long principalId)
            throws NoSuchIdException {
        HashSet<Long> connectedIds = new HashSet<Long>();
        for (UserConnection userConnection : userConnections) {
            connectedIds.add(userConnection.getNonPrincipalId(principalId));
        }
        return connectedIds;
    }
}
