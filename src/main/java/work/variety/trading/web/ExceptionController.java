package work.variety.trading.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangbin
 * @date 2018/3/6 13:40
 */

@ControllerAdvice
public class ExceptionController {
  private static Logger logger = LoggerFactory.getLogger(ExceptionController.class);


  /**
   * 重复操作,重复数据异常处理
   *
   * @param ex exception
   * @return responseBean
   */
  @ExceptionHandler({Exception.class})
  @ResponseStatus(HttpStatus.OK)
  public String duplicateException(Throwable ex) {
    logger.info(ex.getMessage(), ex);
    return "redirect:/";
  }

  /**
   * 获取请求状态
   *
   * @param request request
   * @return status
   */
  private HttpStatus getStatus(HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    if (statusCode == null) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return HttpStatus.valueOf(statusCode);
  }
}
