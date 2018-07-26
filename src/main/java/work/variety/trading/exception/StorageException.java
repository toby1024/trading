package work.variety.trading.exception;

/**
 * @author zhangbin
 * @date 2018/7/25 10:51
 */
public class StorageException extends RuntimeException {
  public StorageException(String message) {
    super(message);
  }

  public StorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
