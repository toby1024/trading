package work.variety.trading.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangbin
 * @date 2018/7/27 10:10
 */
@Data
public class DealInfoDto {

  private int clientInfoId;
  private String name;
  private Date dealDate;
  private Double dealFee;
  private int boardLot;
  private Double commission;
  private Double closeProfit;
  private String dealType;

}
