package work.variety.trading.exception;

/**
 * @author zhangbin
 * @date 2018/7/25 17:29
 */
public class DataParseException extends RuntimeException {
  public DataParseException(String message) {
    super(message);
  }

  public DataParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
