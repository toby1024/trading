package work.variety.trading.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangbin
 * @date 2018/7/31 13:35
 */
@Data
public class PositionStatDto {

  private int clientInfoId;
  private String name;
  private int buyBoardLot;
  private int sellBoardLot;
  private double profit;
  private Date positionDay;
}
