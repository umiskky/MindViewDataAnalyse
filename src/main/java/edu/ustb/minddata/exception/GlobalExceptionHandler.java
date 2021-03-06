package edu.ustb.minddata.exception;

import edu.ustb.minddata.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author UmiSkky
 */
@Slf4j(topic = "exception")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DefinedException.class)
    public ResultBody definedExceptionHandler(DefinedException e){
        log.warn("[Interface] Request error " + e.getResultCode() +": " + e.getResultMsg());
        return ResultBody.error(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultBody exceptionHandler(Exception e){
        log.error("[Interface] Server error: " + e);
        return ResultBody.error(ResultEnum.INTERNAL_SERVER_ERROR);
    }
}
