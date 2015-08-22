package brach.stefan.dae.rest.model.error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ErrMsgWebException extends WebApplicationException {
    private static final long serialVersionUID = 1L;

    public ErrMsgWebException(ErrMsg errMsg) {
        super(Response.status(errMsg.getValue()).entity(errMsg.toString()).build());
    }

    public ErrMsgWebException(int statusCode, String msg) {
        super(Response.status(statusCode).entity(msg).build());
    }
}
