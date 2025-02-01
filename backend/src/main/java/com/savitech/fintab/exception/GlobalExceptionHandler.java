// package com.savitech.fintab.exception;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.FieldError;
// import org.springframework.web.HttpRequestMethodNotSupportedException;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.MissingServletRequestParameterException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// import io.jsonwebtoken.ExpiredJwtException;

// @RestControllerAdvice
// public class GlobalExceptionHandler {
//     @ExceptionHandler(ExpiredJwtException.class)
//     public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token has expired");
//     }

//     // Add more exception handlers for other exceptions if needed

//     // Example of handling generic exceptions
//     @ExceptionHandler(Exception.class)
//     public ResponseEntity<?> handleGenericException(Exception ex) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message(ex.getMessage()));
//     }

//     @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//     public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message(ex.getMessage()));
//     }

//     @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
//     public ResponseEntity<?> handleNoHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException ex) {
//         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message("Resource not found"));
//     }

//     @ExceptionHandler(MissingServletRequestParameterException.class)
//     public ResponseEntity<?> handleMissingParameterException(MissingServletRequestParameterException ex) {
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                 .body(message("Required parameter '" + ex.getParameterName() + "' is missing"));

//     }

//     private Map<String, Object> message(String msg) {
//         Map<String, Object> data = new HashMap<>();
//         data.put("dataResponse", msg);
//         data.put("status", "fail");
//         data.put("statusCode", "01");
//         return data;
//     }

//     @SuppressWarnings("null")
//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
//         StringBuilder stringBuilder = new StringBuilder();
//         e.getBindingResult().getAllErrors().forEach((error) -> {
//             String fieldName;
//             try {
//                 fieldName = ((FieldError) error).getField();
//             } catch (ClassCastException ex) {
//                 fieldName = error.getObjectName();
//             }
//             String message = error.getDefaultMessage();
//             stringBuilder.append(String.format("%s %s ", fieldName, message));
//         });
//         // return new ResponseEntity<>(errorMapper.createErrorMap(stringBuilder.substring(0, stringBuilder.length()-1)), HttpStatus.BAD_REQUEST);

//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message(stringBuilder.substring(0, stringBuilder.length()-1)));
//     }
// }