package spring.tuto.flowdesk.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {

            String message = err.getDefaultMessage();
            response.put("message",message);
        });
        return new ResponseEntity<Map<String,String>>(response,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RessourceNotFoundException.class)
    public ResponseEntity<ResponseStructure> notFoundRequest(RessourceNotFoundException e){

        String message = e.getMessage();

        ResponseStructure response = new ResponseStructure(message,false);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ResponseStructure> badRequestResponse(ApiException e){

        String message = e.getMessage();

        ResponseStructure response = new ResponseStructure(message,false);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseStructure> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e){

        String message = "File is too large! Maximum upload size is 2MB.";

        ResponseStructure response = new ResponseStructure(message,false);

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }

}
