package work.variety.trading.entity;

import lombok.Data;

/**
 * @author zhangbin
 * @date 2018/7/25 14:29
 */
@Data
public class ClientInfo {

  private int id;
  private String futuresCapitalNumber;
  private String name;
  private String companyName;
  private String stockCapitalNumber;
}
