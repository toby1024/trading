package work.variety.trading.exception;

/**
 * @author zhangbin
 * @date 2018/7/25 13:26
 */
public class ErrorFileException extends StorageException {
  public ErrorFileException(String message) {
    super(message);
  }

  public ErrorFileException(String message, Throwable cause) {
    super(message, cause);
  }
}
