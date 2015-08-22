package brach.stefan.dae.model.extractor;

import java.util.HashSet;
import java.util.List;

import brach.stefan.dae.exception.NoSuchIdException;
import brach.stefan.dae.model.UserConnection;

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
