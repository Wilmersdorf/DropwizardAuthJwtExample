package brach.stefan.dae.rest.model.send.validator;

import org.apache.commons.lang3.StringUtils;

import brach.stefan.dae.rest.model.error.ErrMsg;
import brach.stefan.dae.rest.model.error.ErrMsgWebException;
import brach.stefan.dae.rest.model.send.LoginUserDto;

public class LoginUserDtoValidator {
    public static void validate(LoginUserDto loginUserDto) {
        if (loginUserDto == null) {
            throw new ErrMsgWebException(ErrMsg.LOGIN_FAIL_NO_CREDENTIALS);
        }
        if (StringUtils.isBlank(loginUserDto.getEmail())) {
            throw new ErrMsgWebException(ErrMsg.LOGIN_FAIL_NO_EMAIL);
        } else if (StringUtils.isBlank(loginUserDto.getPassword())) {
            throw new ErrMsgWebException(ErrMsg.LOGIN_FAIL_NO_PASSWORD);
        }
    }
}
