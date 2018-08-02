package work.variety.trading.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangbin
 * @date 2018/8/1 15:40
 */
@Data
public class SearchDayAccountDto {
  private int clientInfoId;
  private Date accountDay;

}
