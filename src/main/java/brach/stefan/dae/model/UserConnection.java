package brach.stefan.dae.model;

import brach.stefan.dae.exception.IdConflictException;
import brach.stefan.dae.exception.NoSuchIdException;

public class UserConnection {
    private long lowerId;
    private long higherId;

    public UserConnection() {
    }

    public UserConnection(long firstId, long secondId) throws IdConflictException {
        if (firstId < secondId) {
            this.lowerId = firstId;
            this.higherId = secondId;
        } else if (firstId > secondId) {
            this.lowerId = secondId;
            this.higherId = firstId;
        } else {
            throw new IdConflictException();
        }
    }

    public long getLowerId() {
        return lowerId;
    }

    public void setLowerId(long lowerId) {
        this.lowerId = lowerId;
    }

    public long getHigherId() {
        return higherId;
    }

    public void setHigherId(long higherId) {
        this.higherId = higherId;
    }

    public long getNonPrincipalId(long principalId) throws NoSuchIdException {
        if (lowerId == principalId) {
            return higherId;
        } else if (higherId == principalId) {
            return lowerId;
        } else {
            throw new NoSuchIdException();
        }
    }
}
