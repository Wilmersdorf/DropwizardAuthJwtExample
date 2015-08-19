package brach.stefan.tech.test.msg;

public enum ErrMsg {
    SIGNUP_FAIL(ErrCode.BAD_REQUEST.getValue()), //
    SIGNUP_FAIL_NO_CREDENTIALS(ErrCode.BAD_REQUEST.getValue()), //
    SIGNUP_FAIL_NO_EMAIL(ErrCode.BAD_REQUEST.getValue()), //
    SIGNUP_FAIL_INVALID_EMAIL(ErrCode.BAD_REQUEST.getValue()), //
    SIGNUP_FAIL_NO_PASSWORD(ErrCode.BAD_REQUEST.getValue()), //
    SIGNUP_FAIL_USER_ALREADY_EXISTS(ErrCode.CONFLICT.getValue()), //
    LOGIN_FAIL_NO_CREDENTIALS(ErrCode.BAD_REQUEST.getValue()), //
    LOGIN_FAIL_NO_EMAIL(ErrCode.BAD_REQUEST.getValue()), //
    LOGIN_FAIL_NO_PASSWORD(ErrCode.BAD_REQUEST.getValue()), //
    LOGIN_FAIL_USER_NOT_FOUND_OR_WRONG_PASSWORD(ErrCode.FORBIDDEN.getValue()), //
    GET_USER_CONNECTIONS_FAIL(ErrCode.BAD_REQUEST.getValue()), //
    CONNECT_TO_USER_FAIL_INVALID_JSON(ErrCode.BAD_REQUEST.getValue()), //
    CONNECT_TO_USER_FAIL_USER_NOT_FOUND(ErrCode.NOT_FOUND.getValue()), //
    CONNECT_TO_USER_FAIL_CANNOT_CONNECT_TO_ONESELF(ErrCode.BAD_REQUEST.getValue());
    private final int value;

    private ErrMsg(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
