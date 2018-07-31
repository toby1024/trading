package work.variety.trading.entity;

import lombok.Data;

/**
 * @author zhangbin
 * @date 2018/7/31 10:09
 */
@Data
public class User {

  private Integer id;
  private String username;
  private String password;
  private String jsessionId;
  private Integer state;
}
