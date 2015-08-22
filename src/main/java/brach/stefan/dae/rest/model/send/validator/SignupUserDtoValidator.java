package brach.stefan.dae.rest.model.send.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brach.stefan.dae.auth.MyPasswordValidator;
import brach.stefan.dae.rest.model.error.ErrCode;
import brach.stefan.dae.rest.model.error.ErrMsg;
import brach.stefan.dae.rest.model.error.ErrMsgWebException;
import brach.stefan.dae.rest.model.send.SignupUserDto;

public class SignupUserDtoValidator {
    private final static Logger LOG = LoggerFactory.getLogger(SignupUserDtoValidator.class);

    public static void validate(SignupUserDto signupUserDto, MyPasswordValidator myPasswordValidator) {
        if (signupUserDto == null) {
            throw new ErrMsgWebException(ErrMsg.SIGNUP_FAIL_NO_CREDENTIALS);
        } else if (myPasswordValidator == null) {
            LOG.error("MyPasswordValidator is null.");
            throw new ErrMsgWebException(ErrMsg.SIGNUP_FAIL);
        }
        validateEmail(signupUserDto.getEmail());
        validatePassword(signupUserDto.getPassword(), myPasswordValidator);
    }

    private static void validatePassword(String password, MyPasswordValidator myPasswordValidator) {
        if (StringUtils.isBlank(password)) {
            throw new ErrMsgWebException(ErrMsg.SIGNUP_FAIL_NO_PASSWORD);
        }
        RuleResult result = myPasswordValidator.validate(new PasswordData(new String(password)));
        if (!result.isValid()) {
            throw new ErrMsgWebException(ErrCode.BAD_REQUEST.getValue(), myPasswordValidator.getMessages(result).get(0));
        }
    }

    private static void validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new ErrMsgWebException(ErrMsg.SIGNUP_FAIL_NO_EMAIL);
        } else if (!EmailValidator.getInstance().isValid(email)) {
            throw new ErrMsgWebException(ErrMsg.SIGNUP_FAIL_INVALID_EMAIL);
        }
    }
}
