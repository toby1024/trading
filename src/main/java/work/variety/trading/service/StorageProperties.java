package work.variety.trading.service;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @author zhangbin
 * @date 2018/7/25 10:50
 */
@ConfigurationProperties("storage")
public class StorageProperties {

  /**
   * Folder location for storing files
   */
  private String location = "upload-dir";

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
