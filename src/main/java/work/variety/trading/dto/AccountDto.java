package work.variety.trading.dto;

import lombok.Data;
import work.variety.trading.entity.DayAccountSummary;

/**
 * @author zhangbin
 * @date 2018/8/29 09:39
 */
@Data
public class AccountDto extends DayAccountSummary{

  private String name;
}
