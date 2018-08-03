package work.variety.trading.dto;

import lombok.Data;
import work.variety.trading.entity.PositionInfo;

/**
 * @author zhangbin
 * @date 2018/8/2 13:10
 */
@Data
public class PositionInfoDto extends PositionInfo {
  private String name;
}
