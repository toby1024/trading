package work.variety.trading.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhangbin
 * @date 2018/8/29 09:27
 */
@Data
public class SearchAccountDto extends BaseSearchPageDto{
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date endDate;
  private String name;

  private Integer clientInfoId;
  private boolean isPage = true;
}
