package work.variety.trading.exception;

/**
 * @author zhangbin
 * @date 2018/7/25 10:52
 */
public class StorageFileNotFoundException extends StorageException {

  public StorageFileNotFoundException(String message) {
    super(message);
  }

  public StorageFileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
