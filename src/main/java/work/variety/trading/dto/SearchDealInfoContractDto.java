package work.variety.trading.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zhangbin
 * @date 2018/7/27 14:09
 */
@Data
public class SearchDealInfoContractDto extends SearchDealInfoDto {

  private String contract;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date dealDate;
  private Integer clientInfoId;
}
