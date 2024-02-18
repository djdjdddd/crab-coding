package com.coding.crab.common.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : yong
 * @fileName : ValidationException
 * @date : 2024-02-12
 * @description : 유효성 검사에 실패하는 경우 클라이언트에서 해당 정보를 활용할 수 있도록 에러 정보를 담을 수 있는 커스텀 Exception 클래스
 */
@Getter
public class ValidationException extends RuntimeException {

    private List<FieldError> errors = new ArrayList<>();

    public ValidationException() {
    }

    public ValidationException(String message, FieldError fieldError) {
        super(message);
        this.errors.add(fieldError);
    }

    public ValidationException(String message, Throwable cause, FieldError fieldError) {
        super(message, cause);
        this.errors.add(fieldError);
    }

    public ValidationException(String message, List<FieldError> errors) {
        super(message);
        this.errors = errors;
    }

    public ValidationException(String message, Throwable cause, List<FieldError> errors) {
        super(message, cause);
        this.errors = errors;
    }

}
