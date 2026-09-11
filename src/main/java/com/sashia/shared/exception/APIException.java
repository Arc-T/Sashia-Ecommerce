package com.sashia.shared.exception;

public abstract class APIException extends RuntimeException {

    private final String messageKey;
    private final Object[] messageArgs;
    private final ErrorCategory errorCategory;

    protected APIException(String messageKey, ErrorCategory errorCategory) {
        this.messageKey = messageKey;
        this.errorCategory = errorCategory;
        this.messageArgs = null;
    }

    protected APIException(String messageKey, ErrorCategory errorCategory, Object... messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
        this.errorCategory = errorCategory;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

    public ErrorCategory getErrorCategory() {
        return errorCategory;
    }

}
