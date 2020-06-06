package com.zsl.upmall.exception.handler;

import com.zsl.upmall.aid.JsonResult;
import com.zsl.upmall.exception.BusinessException;
import com.zsl.upmall.exception.RequestLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.StringJoiner;

/**
 * @ClassName CommonExceptionHandler
 * @Description 统一异常处理类
 **/
@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

   private JsonResult result = new JsonResult();

   // @ExceptionHandler(value = Exception.class)
    public JsonResult exceptionHandler(Exception e){
        if (e instanceof BusinessException) {
            return result.error(((BusinessException) e).getStatusCode()+"",e.getMessage());
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            return result.error(400,"请求参数类型不匹配");
        } else if (e instanceof ConstraintViolationException) {
            return result.error(400,e.getMessage());
        }else if(e instanceof RequestLimitException){
            return result.error(((BusinessException) e).getStatusCode(),e.getMessage());
        } else if (e instanceof MethodArgumentNotValidException) {
            List<ObjectError> allErrors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
            StringJoiner errorMessage = new StringJoiner(",");
            for (int i = 0 ; i < allErrors.size() ; i++) {
                ObjectError objectError = allErrors.get(i);
                errorMessage.add(objectError.getDefaultMessage());
            }
            return result.error(400,errorMessage.toString());
        }else if(e instanceof RuntimeException){
            return result.error(500+"",e.getMessage());
        } else {
            log.error(e.getMessage());
            e.printStackTrace();
            return result.error(500,"服务器异常");
        }
    }
}
