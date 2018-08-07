package work.variety.trading.service.impl;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import work.variety.trading.dto.CatchParameterDto;
import work.variety.trading.entity.ClientInfo;
import work.variety.trading.entity.DayAccountSummary;
import work.variety.trading.entity.DealInfo;
import work.variety.trading.entity.PositionInfo;
import work.variety.trading.service.CatchDataService;
import work.variety.trading.service.ClientInfoService;
import work.variety.trading.service.DayAccountSummaryService;
import work.variety.trading.service.DealInfoService;
import work.variety.trading.service.PositionInfoService;
import work.variety.trading.util.HttpUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangbin
 * @date 2018/7/31 20:31
 */
@Service
public class CatchDataServiceImpl implements CatchDataService {

  private Logger logger = LoggerFactory.getLogger(CatchDataServiceImpl.class);
  private static final String GET_URL = "https://investorservice.cfmmc.com/customer/setupViewCustomerDetailFromCompanyAuto.do";
  private static final String POST_URL = "https://investorservice.cfmmc.com/customer/setParameter.do";

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean catchData(CatchParameterDto catchParameterDto) throws Exception {
    logger.info("开始抓取数据" + DateFormatUtils.format(catchParameterDto.getStartDate(), "yyyy-MM-dd") + "---" + DateFormatUtils.format(catchParameterDto.getEndDate(), "yyyy-MM-dd"));
    String responseBody = null;
    responseBody = HttpUtil.get(GET_URL, catchParameterDto.getJSessionId());
    responseBody = responseBody.replaceAll("&nbsp;", "");
    Document document = Jsoup.parse(responseBody);
    String token = document.getElementsByAttributeValue("name", "org.apache.struts.taglib.html.TOKEN").get(0).val();
    Map params = new HashMap();
    params.put("org.apache.struts.taglib.html.TOKEN", token);
    params.put("byType", "trade");

    Date catchDate = catchParameterDto.getStartDate();
    Date endDate = catchParameterDto.getEndDate();
    while (!catchDate.after(endDate)) {
      try {
        Calendar cal = Calendar.getInstance();
        cal.setTime(catchDate);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
          catchDate = DateUtils.addDays(catchDate, 1);
          continue;
        }

        params.put("tradeDate", DateFormatUtils.format(catchDate, "yyyy-MM-dd"));
        responseBody = HttpUtil.post(POST_URL, catchParameterDto.getJSessionId(), params).replaceAll("&nbsp;", "");
        document = Jsoup.parse(responseBody);
        Element element = document.getElementById("waitBody");
        Elements elements = element.select("table");
        Elements clientElements = elements.get(1).children().get(0).children();

        Date date = DateUtils.parseDate(StringUtils.trimAllWhitespace(clientElements.get(1).children().get(3).text()), "yyyy-MM-dd");

        // 基本信息
        ClientInfo clientInfo = saveClient(clientElements);

        // 账户基本信息
        saveDayAccountSummary(elements, clientInfo, date);

        // 交易明细
        saveDealInfo(elements, clientInfo, date);

        // 持仓明细
        savePositionInfo(elements, clientInfo, date);
      }catch (Exception e){
        logger.error(e.getMessage(), e);
      }
      catchDate = DateUtils.addDays(catchDate, 1);
    }

    return true;
  }

  private ClientInfo saveClient(Elements clientElements) {

    String clientId = clientElements.get(1).children().get(1).text();
    String name = clientElements.get(2).children().get(1).text();
    String company = clientElements.get(3).children().get(1).text();
    String stockAccount = clientElements.get(3).children().get(3).text();

    ClientInfo clientInfo = new ClientInfo();
    clientInfo.setName(name);
    clientInfo.setFuturesCapitalNumber(clientId);
    clientInfo.setCompanyName(company);
    clientInfo.setStockCapitalNumber(stockAccount);
    return clientInfoService.createOrFind(clientInfo);
  }

  private void saveDayAccountSummary(Elements elements, ClientInfo clientInfo, Date date) {
    Elements accountElements = elements.get(3).children().get(0).children();
    DayAccountSummary accountSummary = new DayAccountSummary();
    accountSummary.setAccountDay(date);
    accountSummary.setClientInfoId(clientInfo.getId());

    String element = accountElements.get(1).children().get(1).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setBalanceBF(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(1).children().get(3).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setClientEquity(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(2).children().get(1).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setDepositWithdrawal(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(2).children().get(3).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setKhqy(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(3).children().get(1).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setRealizedPL(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(3).children().get(3).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setFhbcdje(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(4).children().get(1).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setFhbcdje(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(4).children().get(1).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setDrzqlj(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(4).children().get(3).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setHbcdje(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(5).children().get(1).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setCommission(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(5).children().get(3).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setPledgeAmount(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(6).children().get(1).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setBalanceCF(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(6).children().get(3).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setMarginOccupied(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(7).children().get(1).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setFdyk(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(7).children().get(3).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setKyzj(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    element = accountElements.get(8).children().get(3).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setRiskDegree(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll("%", "").replaceAll(",", ""))));
    }

    element = accountElements.get(9).children().get(3).text();
    if (!StringUtils.isEmpty(element) && !"--".equals(element)) {
      accountSummary.setMarginCall(Double.parseDouble(StringUtils.trimAllWhitespace(element.replaceAll(",", ""))));
    }

    accountService.findOrCreate(accountSummary);
  }

  private void saveDealInfo(Elements elements, ClientInfo clientInfo, Date date) {
    Elements dealElements = elements.get(7).children().get(0).children();
    int size = dealElements.size();
    for (int i = 2; i < size - 1; i++) {
      DealInfo dealInfo = new DealInfo();
      Elements datas = dealElements.get(i).children();

      dealInfo.setDealDate(date);
      dealInfo.setClientInfoId(clientInfo.getId());

      dealInfo.setContract(datas.get(0).text());
      dealInfo.setDealType(datas.get(1).text());
      dealInfo.setSpeculateHedging(datas.get(2).text());

      if (!StringUtils.isEmpty(datas.get(3).text()) && !"--".equals(datas.get(3).text())) {
        dealInfo.setDealPrice(Double.parseDouble(datas.get(3).text().replaceAll(",", "")));
      }

      if (!StringUtils.isEmpty(datas.get(4).text()) && !"--".equals(datas.get(4).text())) {
        dealInfo.setBoardLot(Integer.parseInt(datas.get(4).text()));
      }

      if (!StringUtils.isEmpty(datas.get(5).text()) && !"--".equals(datas.get(5).text())) {
        dealInfo.setDealFee(Double.parseDouble(datas.get(5).text().replaceAll(",", "")));
      }
      dealInfo.setOpenClose(datas.get(6).text());

      if (!StringUtils.isEmpty(datas.get(7).text()) && !"--".equals(datas.get(7).text())) {
        dealInfo.setCommission(Double.parseDouble(datas.get(7).text()));
      }

      if (!StringUtils.isEmpty(datas.get(8).text()) && !"--".equals(datas.get(8).text())) {
        dealInfo.setCloseProfit(Double.parseDouble(datas.get(8).text().replaceAll(",", "")));
      }
      dealInfoService.findOrCreate(dealInfo);
    }

  }

  private void savePositionInfo(Elements elements, ClientInfo clientInfo, Date date) {
    Elements positionElements = elements.get(9).children().get(0).children();
    int size = positionElements.size();
    for (int i = 2; i < size - 1; i++) {
      PositionInfo positionInfo = new PositionInfo();

      Elements datas = positionElements.get(i).children();
      positionInfo.setPositionDay(date);
      positionInfo.setClientInfoId(clientInfo.getId());

      positionInfo.setContract(datas.get(0).text());
      if (!StringUtils.isEmpty(datas.get(1).text()) && !"--".equals(datas.get(1).text())) {
        positionInfo.setBuyBoardLot(Integer.parseInt(datas.get(1).text()));
      }

      if (!StringUtils.isEmpty(datas.get(2).text()) && !"--".equals(datas.get(2).text())) {
        positionInfo.setBuyPrice(Double.parseDouble(datas.get(2).text().replaceAll(",", "")));
      }

      if (!StringUtils.isEmpty(datas.get(3).text())) {
        positionInfo.setSellBoardLot(Integer.parseInt(datas.get(3).text()));
      }
      if (!StringUtils.isEmpty(datas.get(4).text())) {
        positionInfo.setSellPrice(Double.parseDouble(datas.get(4).text().replaceAll(",", "")));
      }

      if (!StringUtils.isEmpty(datas.get(5).text()) && !"--".equals(datas.get(5).text())) {
        positionInfo.setYesterdayPrice(Double.parseDouble(datas.get(5).text().replaceAll(",", "")));
      }

      if (!StringUtils.isEmpty(datas.get(6).text()) && !"--".equals(datas.get(6).text())) {
        positionInfo.setTodayPrice(Double.parseDouble(datas.get(6).text().replaceAll(",", "")));
      }

      if (!StringUtils.isEmpty(datas.get(7).text()) && !"--".equals(datas.get(7).text())) {
        positionInfo.setProfit(Double.parseDouble(datas.get(7).text().replaceAll(",", "")));
      }

      if (!StringUtils.isEmpty(datas.get(8).text()) && !"--".equals(datas.get(8).text())) {
        positionInfo.setDealMargin(Double.parseDouble(datas.get(8).text().replaceAll(",", "")));
      }
      positionInfo.setSpeculateHedging(datas.get(9).text());
      positionInfoService.findOrCreate(positionInfo);
    }

  }

  @Autowired
  private ClientInfoService clientInfoService;
  @Autowired
  private DayAccountSummaryService accountService;
  @Autowired
  private DealInfoService dealInfoService;
  @Autowired
  private PositionInfoService positionInfoService;
}
