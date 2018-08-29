package work.variety.trading.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangbin
 * @date 2018/7/31 15:06
 */
@Data
public class DayAccountSummary {

  private Integer id;
  private Integer clientInfoId;
  /**
   * 期初结存
   */
  private Double balanceBF;
  /**
   * 基础保证金
   */
  private Double initialMargin;
  /**
   * 出 入 金
   */
  private Double depositWithdrawal;
  /**
   * 期末结存
   */
  private Double balanceCF;
  /**
   * 平仓盈亏
   */
  private Double realizedPL;
  /**
   * 质 押 金
   */
  private Double pledgeAmount;
  /**
   * 持仓盯市盈亏
   */
  private Double mtmpl;
  /**
   * 客户权益
   */
  private Double clientEquity;
  /**
   * 期权执行盈亏
   */
  private Double exercisePL;
  /**
   * 货币质押保证金占用
   */
  private Double fXPledgeOcc;
  /**
   * 手 续 费
   */
  private Double commission;
  /**
   * 保证金占用
   */
  private Double marginOccupied;
  /**
   * 行权手续费
   */
  private Double exerciseFee;
  /**
   * 交割保证金
   */
  private Double deliveryMargin;
  /**
   * 交割手续费
   */
  private Double deliveryFee;
  /**
   * 多头期权市值
   */
  private Double marketValueLong;
  /**
   * 货币质入
   */
  private Double newFXPledge;
  /**
   * 空头期权市值
   */
  private Double marketValueShort;
  /**
   * 币质出 FX
   */
  private Double fxRedemption;
  /**
   * 市值权益
   */
  private Double marketValueEquity;
  /**
   * 质押变化金额
   */
  private Double chgInPledgeAmt;
  /**
   * 可用资金
   */
  private Double fundAvail;
  /**
   * 权利金收入
   */
  private Double premiumReceived;
  /**
   * 风 险 度
   */
  private Double riskDegree;
  /**
   * 利金支出
   */
  private Double premiumPaid;
  /**
   * 应追加资金
   */
  private Double marginCall;
  /**
   * 货币质押变化金额
   */
  private Double chgInFXPledge;
  /**
   * 非货币充抵金额
   */
  private Double fhbcdje;

  /**
   *  当日总权利金
   */
  private Double drzqlj;
  /**
   *   货币充抵金额
   */
  private Double hbcdje;
  /**
   * 浮动盈亏
   */
  private Double fdyk;
  /**
   * 日期
   */
  private Date accountDay;
}
