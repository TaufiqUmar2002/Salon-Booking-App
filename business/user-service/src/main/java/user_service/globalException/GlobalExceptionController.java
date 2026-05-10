//package user_service.globalException;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import user_service.payload.response.ExceptionResponse;
//
//import java.time.LocalDateTime;
//
//@RestControllerAdvice
//public class GlobalExceptionController {
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception e, WebRequest wb){
//        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
//                .error(e.getMessage())
//                .error(wb.getDescription(false))
//                .timeStamp(LocalDateTime.now()).build();
//        return ResponseEntity.ok(exceptionResponse);
//    }
//}
