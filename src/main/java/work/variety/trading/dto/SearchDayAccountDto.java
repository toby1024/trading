package work.variety.trading.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhangbin
 * @date 2018/8/1 15:40
 */
@Data
public class SearchDayAccountDto {
  private int clientInfoId;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date accountDay;

}
