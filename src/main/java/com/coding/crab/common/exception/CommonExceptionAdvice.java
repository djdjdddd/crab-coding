package com.coding.crab.common.exception;

import com.coding.crab.domain.signup.request.SignupRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : yong
 * @fileName : CommonExceptionAdvice
 * @date : 2024-02-12
 * @description : @Controller 에서 발생한 예외를 잡아 처리해주는 클래스
 */
@Slf4j
@ControllerAdvice
@RestController
public class CommonExceptionAdvice {

    /**
     * 유효성 검사 실패시 발생하는 예외인 ValidationException 을 처리해주는 ExceptionHandler 이다.
     * @param e ValidationException
     * @return ErrorResponse(400, e.getMessage(), errors)
     * @see ValidationException
     * @see com.coding.crab.domain.signup.controller.SignupController#signup(SignupRequestDTO, BindingResult)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ValidationException.class})
    public ErrorResponse validationExceptionHandler(ValidationException e) {
        log.error(e.getMessage(), e);
        List<FieldError> errors = e.getErrors();
        return new ErrorResponse(400, e.getMessage(), errors);
    }

    @Getter
    @NoArgsConstructor
    public static class ErrorResponse {
        private int code;
        private String message;
        private List<FieldError> errors;

        public ErrorResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public ErrorResponse(int code, String message, List<FieldError> errors) {
            this.code = code;
            this.message = message;
            this.errors = errors;
        }
    }

}
