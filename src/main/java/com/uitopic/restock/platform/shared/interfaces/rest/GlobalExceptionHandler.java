package com.uitopic.restock.platform.shared.interfaces.rest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.uitopic.restock.platform.resources.domain.exception.NameAlreadyExist;
import com.uitopic.restock.platform.shared.domain.exceptions.InvalidCredentialsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;
import com.uitopic.restock.platform.planning.domain.exception.IngredientAdditionConflictException;
import com.uitopic.restock.platform.planning.domain.exception.IngredientNotFoundException;
import com.uitopic.restock.platform.planning.domain.exception.InvalidProductTypeException;
import com.uitopic.restock.platform.planning.domain.exception.ProductAlreadyExistsException;
import com.uitopic.restock.platform.planning.domain.exception.ProductNotFoundException;
import com.uitopic.restock.platform.resources.domain.exception.CustomSupplyNotFoundException;
import com.uitopic.restock.platform.sales.domain.exceptions.InsufficientStockException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST controllers.
 *
 * Converts application, validation and request binding errors into consistent
 * HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions with an explicit HTTP status.
     *
     * @param ex response status exception
     * @param request HTTP request
     * @return structured error response
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        return ResponseEntity.status(status).body(errorBody(
                status,
                ex.getReason(),
                request.getRequestURI()
        ));
    }

    /**
     * Handles validation errors from JSON request bodies.
     *
     * @param ex validation exception
     * @param request HTTP request
     * @return structured validation error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = errorBody(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request.getRequestURI()
        );

        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Handles binding errors from form-data and multipart requests.
     *
     * @param ex bind exception
     * @param request HTTP request
     * @return structured validation error response
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, Object>> handleBindException(
            BindException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            if ("image".equals(error.getField())) {
                errors.put("image", "Image must be uploaded as a file or omitted");
            } else {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }

        Map<String, Object> body = errorBody(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request.getRequestURI()
        );

        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Handles invalid arguments thrown by commands, value objects or assemblers.
     *
     * @param ex illegal argument exception
     * @param request HTTP request
     * @return structured error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(errorBody(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    /**
     * Handles invalid state transitions in domain aggregates (e.g. confirm before all steps complete).
     *
     * @param ex illegal state exception
     * @param request HTTP request
     * @return structured error response
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(
            IllegalStateException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(errorBody(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    /**
     * Handles missing query parameters.
     *
     * @param ex missing parameter exception
     * @param request HTTP request
     * @return structured error response
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParameterException(
            MissingServletRequestParameterException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(errorBody(
                HttpStatus.BAD_REQUEST,
                "Missing required query parameter: " + ex.getParameterName(),
                request.getRequestURI()
        ));
    }

    /**
     * Handles invalid query or path parameter types.
     *
     * @param ex type mismatch exception
     * @param request HTTP request
     * @return structured error response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(errorBody(
                HttpStatus.BAD_REQUEST,
                "Invalid value for parameter '" + ex.getName() + "'",
                request.getRequestURI()
        ));
    }

    /**
     * Handles malformed JSON or invalid JSON value formats.
     *
     * @param ex message not readable exception
     * @param request HTTP request
     * @return structured error response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        InvalidFormatException invalidFormatException = findInvalidFormatException(ex);

        if (invalidFormatException != null) {
            return ResponseEntity.badRequest().body(errorBody(
                    HttpStatus.BAD_REQUEST,
                    "Invalid value '" + invalidFormatException.getValue()
                            + "' for type "
                            + invalidFormatException.getTargetType().getSimpleName(),
                    request.getRequestURI()
            ));
        }

        return ResponseEntity.badRequest().body(errorBody(
                HttpStatus.BAD_REQUEST,
                "Invalid request body",
                request.getRequestURI()
        ));
    }

    /**
     * Handles authentication failures caused by invalid credentials.
     *
     * @param ex invalid credentials exception
     * @param request HTTP request
     * @return structured error response
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(
            InvalidCredentialsException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    /**
     * Handles duplicated resource names.
     *
     * @param ex name already exists exception
     * @param request HTTP request
     * @return structured error response
     */
    @ExceptionHandler(NameAlreadyExist.class)
    public ResponseEntity<Map<String, Object>> handleNameAlreadyExistException(
            NameAlreadyExist ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    /**
     * Handles file upload size errors.
     *
     * @param ex max upload size exception
     * @param request HTTP request
     * @return structured error response
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorBody(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "Uploaded file is too large",
                request.getRequestURI()
        ));
    }

    /**
     * Handles unexpected errors.
     *
     * @param ex exception
     * @param request HTTP request
     * @return structured error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected server error",
                request.getRequestURI()
        ));
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFoundException(
            ProductNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(
                HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(CustomSupplyNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomSupplyNotFoundException(
            CustomSupplyNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorBody(
                HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request.getRequestURI()));
    }

    /**
     * Handles insufficient physical stock while completing a sales order.
     * Returns 422 with the structured needed/available payload the Angular
     * "Action Blocked: Insufficient Physical Inventory" dialog renders.
     *
     * @param ex insufficient stock exception
     * @param request HTTP request
     * @return structured error response with stock details
     */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientStockException(
            InsufficientStockException ex, HttpServletRequest request) {
        Map<String, Object> body = errorBody(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage(),
                request.getRequestURI()
        );

        body.put("customSupplyId", ex.getCustomSupplyId());
        body.put("supplyName", ex.getSupplyName());
        body.put("quantityNeeded", ex.getQuantityNeeded());
        body.put("quantityAvailable", ex.getQuantityAvailable());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    @ExceptionHandler(IngredientAdditionConflictException.class)
    public ResponseEntity<Map<String, Object>> handleIngredientAdditionConflictException(
            IngredientAdditionConflictException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody(
                HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleIngredientNotFoundException(
            IngredientNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(
                HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(InvalidProductTypeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidProductTypeException(
            InvalidProductTypeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorBody(
                HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleProductAlreadyExistsException(
            ProductAlreadyExistsException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody(
                HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()));
    }
    private Map<String, Object> errorBody(HttpStatus status, String message, String path) {
        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);

        return body;
    }

    private InvalidFormatException findInvalidFormatException(Throwable throwable) {
        Throwable current = throwable;

        while (current != null) {
            if (current instanceof InvalidFormatException invalidFormatException) {
                return invalidFormatException;
            }

            current = current.getCause();
        }

        return null;
    }
}