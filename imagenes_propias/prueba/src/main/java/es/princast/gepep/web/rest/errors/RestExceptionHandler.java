package es.princast.gepep.web.rest.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({ NoSuchElementException.class })
  public ResponseEntity<Object> handleEntityNotFound(NoSuchElementException ex, WebRequest request) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler({ IllegalArgumentException.class })
  public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
    return ResponseEntity.badRequest().build();
  }

}
