package brach.stefan.dae.rest.model.send.validator;

import org.apache.commons.lang3.StringUtils;

import brach.stefan.dae.rest.model.error.ErrMsg;
import brach.stefan.dae.rest.model.error.ErrMsgWebException;
import brach.stefan.dae.rest.model.send.ConnectToUserDto;

public class ConnectToUserDtoValidator {
    public static void validate(ConnectToUserDto connectToUserDto) {
        if (connectToUserDto == null) {
            throw new ErrMsgWebException(ErrMsg.CONNECT_TO_USER_FAIL_INVALID_JSON);
        } else if (StringUtils.isBlank(connectToUserDto.getEmail())) {
            throw new ErrMsgWebException(ErrMsg.CONNECT_TO_USER_FAIL_INVALID_JSON);
        }
    }
}
