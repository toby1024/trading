package work.variety.trading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangbin
 * @date 2018/9/6 23:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatDto {

  private String statDate;
  private Long clientInfoId;
  private Double commission;
  private String name;
  private Double depositWithdrawal;
}
