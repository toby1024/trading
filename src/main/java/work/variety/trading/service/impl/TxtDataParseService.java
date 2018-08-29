package work.variety.trading.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import work.variety.trading.entity.ClientInfo;
import work.variety.trading.entity.DayAccountSummary;
import work.variety.trading.entity.DealInfo;
import work.variety.trading.entity.PositionInfo;
import work.variety.trading.service.ClientInfoService;
import work.variety.trading.service.DayAccountSummaryService;
import work.variety.trading.service.DealInfoService;
import work.variety.trading.service.FileParseService;
import work.variety.trading.service.PositionInfoService;
import work.variety.trading.service.StorageProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangbin
 * @date 2018/7/31 14:13
 */
@Slf4j
@Service("txtDataParseService")
public class TxtDataParseService implements FileParseService {

  private static final String DATE_PARSE_STR = "YYYYMMdd";
  private static final String DATETIME_PARSE_STR = "YYYY-MM-dd HH:mm:ss";
  Pattern pattern = Pattern.compile("^\\|\\d{8}\\|");


  private final Path rootLocation;

  @Autowired
  public TxtDataParseService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void parse(String filename) {
    File file = new File(this.rootLocation + "/" + filename);
    try {
      Date date = new Date();

      List<String> lines = FileUtils.readLines(file, "gbk");
      boolean isClient = false;
      boolean isAccount = false;
      boolean isDeal = false;
      boolean isPosition = false;
      boolean firstPosition = true;
      boolean needDelete = false;

      ClientInfo clientInfo = new ClientInfo();
      DayAccountSummary dayAccountSummary = new DayAccountSummary();

      log.info("开始解析txt文件：" + filename);
      for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);

        // 交易结算单
        if (line.contains("交易结算单(盯市) Settlement Statement(MTM)")) {
          isClient = true;
        }
        if (isClient) {
          log.info("--------解析client info--------");
          if (line.startsWith("客户号 Client ID")) {
            clientInfo = saveClientInfo(line);
          }
          if (line.startsWith("日期")) {
            needDelete = true;
            String[] array = line.split("：");
            date = DateUtils.parseDate(StringUtils.trimAllWhitespace(array[1]), DATE_PARSE_STR);
          }
        }

        if (needDelete) {
          needDelete = deleteHistory(clientInfo, date);
        }

        //资金情况
        if (line.contains("资金状况  币种：人民币  Account Summary  Currency：CNY")) {
          isClient = false;
          isAccount = true;
        }
        if (isAccount) {
          log.info("--------解析资金情况--------");
          String first = "期初结存 Balance b/f：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "基础保证金 Initial Margin：", line);
            dayAccountSummary.setBalanceBF(datas[0]);
            dayAccountSummary.setInitialMargin(datas[1]);
          }

          first = "出 入 金 Deposit/Withdrawal：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "期末结存 Balance c/f：", line);
            dayAccountSummary.setDepositWithdrawal(datas[0]);
            dayAccountSummary.setBalanceCF(datas[1]);
          }

          first = "平仓盈亏 Realized P/L：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "质 押 金 Pledge Amount：", line);
            dayAccountSummary.setRealizedPL(datas[0]);
            dayAccountSummary.setPledgeAmount(datas[1]);
          }

          first = "持仓盯市盈亏 MTM P/L：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "客户权益 Client Equity：：", line);
            dayAccountSummary.setMtmpl(datas[0]);
            dayAccountSummary.setClientEquity(datas[1]);
          }

          first = "期权执行盈亏 Exercise P/L：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "货币质押保证金占用 FX Pledge Occ.：", line);
            dayAccountSummary.setExercisePL(datas[0]);
            dayAccountSummary.setFXPledgeOcc(datas[1]);
          }

          first = "手 续 费 Commission：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "保证金占用 Margin Occupied：", line);
            dayAccountSummary.setCommission(datas[0]);
            dayAccountSummary.setMarginOccupied(datas[1]);
          }

          first = "行权手续费 Exercise Fee：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "交割保证金 Delivery Margin：", line);
            dayAccountSummary.setExerciseFee(datas[0]);
            dayAccountSummary.setDeliveryMargin(datas[1]);
          }

          first = "交割手续费 Delivery Fee：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "多头期权市值 Market value(long)：", line);
            dayAccountSummary.setDeliveryFee(datas[0]);
            dayAccountSummary.setMarketValueLong(datas[1]);
          }

          first = "货币质入 New FX Pledge：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "空头期权市值 Market value(short)：", line);
            dayAccountSummary.setNewFXPledge(datas[0]);
            dayAccountSummary.setMarketValueShort(datas[1]);
          }

          first = "货币质出 FX Redemption：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "市值权益 Market value(equity)：", line);
            dayAccountSummary.setFxRedemption(datas[0]);
            dayAccountSummary.setMarketValueEquity(datas[1]);
          }

          first = "质押变化金额 Chg in Pledge Amt：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "可用资金 Fund Avail.：", line);
            dayAccountSummary.setChgInPledgeAmt(datas[0]);
            dayAccountSummary.setFundAvail(datas[1]);
          }

          first = "质押变化金额 Chg in Pledge Amt：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "可用资金 Fund Avail.：", line);
            dayAccountSummary.setChgInPledgeAmt(datas[0]);
            dayAccountSummary.setFundAvail(datas[1]);
          }

          first = "权利金收入 Premium received：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "风 险 度 Risk Degree：", line);
            dayAccountSummary.setPremiumReceived(datas[0]);
            dayAccountSummary.setRiskDegree(datas[1]);
          }

          first = "权利金支出 Premium paid：";
          if (line.startsWith(first)) {
            double[] datas = getData(first, "应追加资金 Margin Call：", line);
            dayAccountSummary.setPremiumPaid(datas[0]);
            dayAccountSummary.setMarginCall(datas[1]);
          }

          first = "货币质押变化金额 Chg in FX Pledge:";
          if (line.startsWith(first)) {
            String data = line.replaceAll(first, "");
            data = StringUtils.trimAllWhitespace(data);
            dayAccountSummary.setChgInFXPledge(Double.parseDouble(data));
          }

          dayAccountSummary.setAccountDay(date);
          dayAccountSummary.setClientInfoId(clientInfo.getId());
        }

        if (line.contains("成交记录 Transaction Record")) {
          isAccount = false;
          isClient = false;
          isDeal = true;
        }
        if (isDeal) {
          log.info("--------解析交易情况--------");
          saveDealInfo(line, clientInfo);
        }

        if (line.contains("持仓汇总 Positions")) {
          isAccount = false;
          isClient = false;
          isDeal = false;
          isPosition = true;
        }
        if (isPosition) {
          log.info("--------解析持仓情况--------");
          if (firstPosition) {
            if (StringUtils.isEmpty(StringUtils.trimAllWhitespace(lines.get(i + 1)))) {
              i = i + 10;
            } else {
              i = i + 5;
            }
            line = lines.get(i);
            firstPosition = false;
          }
          if (line.startsWith("-------") && line.endsWith("-------")) {
            break;
          }
          savePositionInfo(line, clientInfo, date);
        }
      }
      dayAccountSummaryService.deleteByClientAndDate(clientInfo.getId(), date);
      saveDayAccountSummary(dayAccountSummary);
      log.info("-------解析txt文件完成--------");
    } catch (IOException e) {
      e.printStackTrace();
      log.error(e.getMessage(), e);
    } catch (ParseException e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }
  }

  private double[] getData(String first, String second, String line) {
    first = first.replaceAll("\\(", "#").replaceAll("\\)", "#");
    String data = line.replaceAll("\\(", "#")
      .replaceAll("\\)", "#")
      .replaceAll(first, "")
      .replaceAll("%", "");

    second = second.replaceAll("\\(", "#").replaceAll("\\)", "#");
    data = data.replaceAll(second, "###");

    data = StringUtils.trimAllWhitespace(data);
    String[] datas = data.split("###");
    return new double[]{Double.parseDouble(datas[0]), Double.parseDouble(datas[1])};
  }

  private void saveDealInfo(String line, ClientInfo clientInfo) throws ParseException {
    Matcher matcher = pattern.matcher(line);
    if (matcher.find() && matcher.start() == 0) {
      line = StringUtils.trimAllWhitespace(line);
      String[] datas = line.split("\\|");
      DealInfo dealInfo = new DealInfo();
      dealInfo.setDealDate(DateUtils.parseDate(datas[1], DATE_PARSE_STR));
      dealInfo.setExchange(datas[2]);
      dealInfo.setProduct(datas[3]);
      dealInfo.setContract(datas[4]);
      dealInfo.setDealType(datas[5]);
      dealInfo.setSpeculateHedging(datas[6]);
      dealInfo.setDealPrice(Double.parseDouble(datas[7]));
      dealInfo.setBoardLot(Integer.parseInt(datas[8]));
      dealInfo.setDealFee(Double.parseDouble(datas[9]));
      dealInfo.setOpenClose(datas[10]);
      dealInfo.setCommission(Double.parseDouble(datas[11]));
      dealInfo.setCloseProfit(Double.parseDouble(datas[12]));
      dealInfo.setRealizedReceivedPaid(Double.parseDouble(datas[13]));
      dealInfo.setDealNumber(datas[14]);
      dealInfo.setClientInfoId(clientInfo.getId());
      dealInfoService.forceCreate(dealInfo);
    }
  }

  private void savePositionInfo(String line, ClientInfo clientInfo, Date date) {
    if (StringUtils.isEmpty(line)) {
      return;
    }
    String[] datas = StringUtils.trimAllWhitespace(line).split("\\|");
    PositionInfo positionInfo = new PositionInfo();
    positionInfo.setProduct(datas[1]);
    positionInfo.setContract(datas[2]);
    positionInfo.setBuyBoardLot(Integer.parseInt(datas[3]));
    positionInfo.setBuyPrice(Double.parseDouble(datas[4]));
    positionInfo.setSellBoardLot(Integer.parseInt(datas[5]));
    positionInfo.setSellPrice(Double.parseDouble(datas[6]));
    positionInfo.setYesterdayPrice(Double.parseDouble(datas[7]));
    positionInfo.setTodayPrice(Double.parseDouble(datas[8]));
    positionInfo.setProfit(Double.parseDouble(datas[9]));
    positionInfo.setDealMargin(Double.parseDouble(datas[10]));
    positionInfo.setSpeculateHedging(datas[11]);
    positionInfo.setMarketValueLong(Double.parseDouble(datas[12]));
    positionInfo.setMarketValueShort(Double.parseDouble(datas[13]));
    positionInfo.setClientInfoId(clientInfo.getId());
    positionInfo.setPositionDay(date);
    positionInfoService.forceCreate(positionInfo);
  }

  private ClientInfo saveClientInfo(String line) {
    ClientInfo clientInfo = new ClientInfo();
    String[] array = line.split("客户名称 Client Name：");
    String[] clientIdArray = array[0].split("：");
    clientInfo.setFuturesCapitalNumber(StringUtils.trimAllWhitespace(clientIdArray[1]));
    clientInfo.setName(StringUtils.trimAllWhitespace(array[1]));
    return clientInfoService.createOrFind(clientInfo);
  }

  private DayAccountSummary saveDayAccountSummary(DayAccountSummary dayAccountSummary) {
    return dayAccountSummaryService.forceCreate(dayAccountSummary);
  }

  private boolean deleteHistory(ClientInfo clientInfo, Date date) {
    dealInfoService.deleteByClientAndDate(clientInfo.getId(), date);
    positionInfoService.deleteByClientAndDate(clientInfo.getId(), date);
    dayAccountSummaryService.deleteByClientAndDate(clientInfo.getId(), date);
    return false;
  }

  @Autowired
  private ClientInfoService clientInfoService;
  @Autowired
  private DayAccountSummaryService dayAccountSummaryService;
  @Autowired
  private DealInfoService dealInfoService;
  @Autowired
  private PositionInfoService positionInfoService;
}
