package work.variety.trading.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangbin
 * @date 2018/7/25 14:44
 */
@Data
public class PositionInfo {
  private int id;
  /**
   * 合约
   */
  private  String contract;
  /**
   * 成交序号
   */
  private String dealNumber;
  /**
   * 买持仓
   */
  private int buyBoardLot;
  /**
   * 买入价
   */
  private double buyPrice;
  /**
   * 卖持仓
   */
  private int sellBoardLot;
  /**
   * 卖出价
   */
  private Double sellPrice;
  /**
   * 昨结算价
   */
  private Double yesterdayPrice;
  /**
   * 今结算价
   */
  private Double todayPrice;
  /**
   * 浮动盈亏
   */
  private Double profit;
  /**
   * 投机/套保
   */
  private String speculateHedging;
  /**
   * 交易编码
   */
  private String transactionNumber;
  /**
   * 实际成交日期
   */
  private Date realDealDate;
  /**
   * 关联的客户id
   */
  private int clientInfoId;
}
