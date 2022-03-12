package popkov.dmitrii.captcha.aspects;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import popkov.dmitrii.captcha.dto.error.UnauthorizedMessage;
import popkov.dmitrii.captcha.dto.error.ValidationErrorResponse;
import popkov.dmitrii.captcha.dto.error.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public UnauthorizedMessage onUnauthorizedException(HttpClientErrorException.Unauthorized e) {
        return new UnauthorizedMessage("Could not authorize");
    }
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onBindException(BindException e) {
        ValidationErrorResponse errors = new ValidationErrorResponse();
        for (FieldError error : e.getFieldErrors()) {
            errors.getViolations().add(
                    new Violation(error.getField(), String.format("failed to bind value %s",error.getRejectedValue()))
            );
        }
        return errors;
    }
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.getViolations().add(
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return error;
    }

}
