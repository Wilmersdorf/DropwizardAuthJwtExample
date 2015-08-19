package brach.stefan.tech.test.rest.model.send.validator;

import org.apache.commons.lang3.StringUtils;

import brach.stefan.tech.test.msg.ErrMsg;
import brach.stefan.tech.test.msg.ErrMsgWebException;
import brach.stefan.tech.test.rest.model.send.ConnectToUserDto;

public class ConnectToUserDtoValidator {
    public static void validate(ConnectToUserDto connectToUserDto) {
        if (connectToUserDto == null) {
            throw new ErrMsgWebException(ErrMsg.CONNECT_TO_USER_FAIL_INVALID_JSON);
        } else if (StringUtils.isBlank(connectToUserDto.getEmail())) {
            throw new ErrMsgWebException(ErrMsg.CONNECT_TO_USER_FAIL_INVALID_JSON);
        }
    }
}
