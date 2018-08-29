package work.variety.trading.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangbin
 * @date 2018/7/25 14:57
 */
@Data
public class DealInfo {
  private int id;
  /**
   * 交易日期
   */
  private Date dealDate;
  /**
   * 合约
   */
  private String contract;
  /**
   * 成交序号
   */
  private String dealNumber;
  /**
   * 买/卖
   */
  private String dealType;
  /**
   * 投机/套保
   */
  private String speculateHedging;
  /**
   * 成交价
   */
  private Double dealPrice;
  /**
   * 手数
   */
  private int boardLot;
  /**
   * 成交额
   */
  private Double dealFee;
  /**
   * 开/平
   */
  private String openClose;
  /**
   * 手续费
   */
  private Double commission;
  /**
   * 平仓盈亏
   */
  private Double closeProfit;
  /**
   * 实际成交日期
   */
  private Date realDealDate;
  /**
   * 客户id
   */
  private int clientInfoId;
  /**
   * 交易所
   */
  private String exchange;
  /**
   * 品种
   */
  private String product;
  /**
   * 权利金收支
   */
  private Double realizedReceivedPaid;
}
