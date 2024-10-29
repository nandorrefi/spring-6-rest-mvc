package guru.springframework.spring6restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice   // this is going to be a global exception handler for every controller
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)  // whatever throws a NotFoundException will be handled by this method
    public ResponseEntity handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}
