package work.variety.trading.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import work.variety.trading.exception.DataParseException;
import work.variety.trading.exception.ErrorFileException;

/**
 * @author zhangbin
 * @date 2018/7/26 15:39
 */
@ControllerAdvice
public class TradingExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({ErrorFileException.class})
  protected ResponseEntity<Object> handleFileError(
    Exception ex, WebRequest request) {
    return handleExceptionInternal(ex, "excel文件格式错误",
      new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler({DataParseException.class})
  protected ResponseEntity<Object> handleDataError(
    Exception ex, WebRequest request) {
    return handleExceptionInternal(ex, "excel解析出错",
      new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }
}
