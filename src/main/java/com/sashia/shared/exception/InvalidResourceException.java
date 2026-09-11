package com.sashia.shared.exception;

public class InvalidResourceException extends APIException {

    public InvalidResourceException(String messageKey) {
        super(messageKey, ErrorCategory.NOT_FOUND);
    }

}
