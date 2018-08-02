package work.variety.trading.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhangbin
 * @date 2018/7/31 16:49
 */
@Data
public class CatchParameterDto {
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date endDate;
  private String jSessionId;
}
