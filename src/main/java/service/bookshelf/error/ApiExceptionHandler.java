package service.bookshelf.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity handleMaxSizeException(Exception ex, WebRequest request) {
        return prepareRestException(ex, request, HttpStatus.PAYLOAD_TOO_LARGE);
    }
    @ExceptionHandler({UploadImageException.class})
    public ResponseEntity<Object> handleUploadImageException(Exception ex, WebRequest request) {
        return prepareRestException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request) {
        return prepareRestException(ex, request, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({EntityAlreadyExistsException.class})
    public ResponseEntity<Object> handleAlreadyExistsException(Exception ex, WebRequest request) {
        return prepareRestException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MultipartException.class})
    public ResponseEntity<Object> handleMultipartException(Exception ex, WebRequest request) {
        return prepareRestException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<Object> handleApplicationException(Exception ex, WebRequest request) {
        return prepareRestException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        final ApiErrorResponse response = ApiErrorResponse.valueOf(
                HttpStatus.BAD_REQUEST.value(),
                getPath(request),
                "Validation error",
                ex.getClass().getName());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return prepareRestException(ex, request, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> prepareRestException(Exception ex, WebRequest request, HttpStatus status) {
        logger.info(ex.getClass().getName());
        String error = ex.getLocalizedMessage();
        if (ex.getCause() != null) {
            error += String.format(". %s", ex.getCause().getMessage());
        } else if (error.isEmpty()) {
            error = "message not available";
        }
        final ApiErrorResponse response = ApiErrorResponse.valueOf(status.value(), getPath(request), error, ex.getClass().getName());
        return new ResponseEntity<>(response, new HttpHeaders(), status);
    }

    private String getPath(WebRequest request) {
        return ((ServletWebRequest) request)
                .getRequest()
                .getRequestURI();
    }
}
