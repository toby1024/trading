package work.variety.trading.dto;

import lombok.Data;
import work.variety.trading.entity.DealInfo;

/**
 * @author zhangbin
 * @date 2018/7/30 15:39
 */
@Data
public class DealInfoDetailDto extends DealInfo {
  private String name;
}
