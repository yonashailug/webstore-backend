package edu.miu.webstorebackend.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorDTO processValidationError(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    //TODO: - Check with Not found error message complaint
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundDTO processNotFoundError(NotFoundException e) {
        NotFoundDTO dto = new NotFoundDTO();
        dto.setMessage(e.getMessage());
        dto.setName("id");
        return dto;
    }

    private ValidationErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
        ValidationErrorDTO dto = new ValidationErrorDTO("ValidationError");
        for (FieldError fieldError : fieldErrors) {
            dto.addFieldError(fieldError.getField(), messageSourceAccessor.getMessage(fieldError));
        }
        return dto;
    }

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public NotFoundDTO handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new NotFoundDTO(
                "Refresh Token",
                ex.getMessage()
        );
    }
}
