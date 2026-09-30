package com.sashia.shared.exception;

public abstract class APIException extends RuntimeException {

    private final String messageKey;
    private final Object[] messageArgs;

    protected APIException(String messageKey) {
        this.messageKey = messageKey;
        this.messageArgs = null;
    }

    protected APIException(String messageKey, Object... messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

}
