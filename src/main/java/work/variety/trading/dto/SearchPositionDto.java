package work.variety.trading.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhangbin
 * @date 2018/7/31 13:36
 */
@Data
public class SearchPositionDto extends BaseSearchPageDto{
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date endDate;
  private String name;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date positionDay;

  private Integer clientInfoId;
  private boolean isPage = true;
}
