package work.variety.trading.service;

import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import work.variety.trading.entity.ClientInfo;
import work.variety.trading.entity.DealInfo;
import work.variety.trading.entity.PositionInfo;
import work.variety.trading.util.HttpUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin
 * @date 2018/7/26 20:55
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TestCatchData {

  @Test
  public void catchData() throws Exception {
    String url = "https://investorservice.cfmmc.com/customer/setupViewCustomerDetailFromCompanyAuto.do";
    String jSessionid = "02nz-NpvOz4QGednMmS0Pg4rhr26HzGFeM3J07qx1uTQ1JgFRMmn!-103551700";

    String responseBody = HttpUtil.get(url, jSessionid);
    responseBody = responseBody.replaceAll("&nbsp;", "");

    Document document = Jsoup.parse(responseBody);

    String token = document.getElementsByAttributeValue("name", "org.apache.struts.taglib.html.TOKEN").get(0).val();

    url = "https://investorservice.cfmmc.com/customer/setParameter.do";
    Map params = new HashMap();
    params.put("org.apache.struts.taglib.html.TOKEN", token);
    params.put("tradeDate", "2018-07-04");
    params.put("byType", "trade");
    responseBody = HttpUtil.post(url, jSessionid, params);

    responseBody = responseBody.replaceAll("&nbsp;", "");

    document = Jsoup.parse(responseBody);
    Element element = document.getElementById("waitBody");
    Elements elements = element.select("table");
    Elements clientElements = elements.get(1).children().get(0).children();

    String clientId = clientElements.get(1).children().get(1).text();
    Date date = DateUtils.parseDate(clientElements.get(1).children().get(3).text(), "yyyy-MM-dd");
    String name = clientElements.get(2).children().get(1).text();
    String company = clientElements.get(3).children().get(1).text();
    String account = clientElements.get(3).children().get(3).text();

    ClientInfo clientInfo = new ClientInfo();

    System.out.println(clientId + "->" + date);
    System.out.println(name + "->" + company + "->" + account);

    Elements accountElements = elements.get(3).children().get(0).children();
    double balanceBF = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(1).children().get(1).text().replaceAll(",", "")));
    double clientEquity = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(1).children().get(3).text().replaceAll(",", "")));
    double depositWithdrawal = 0d;
    try {
      depositWithdrawal = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(2).children().get(1).text().replaceAll(",", "")));
    } catch (Exception e) {

    }
    double khqy = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(2).children().get(3).text().replaceAll(",", "")));
    double realizedPL = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(3).children().get(1).text().replaceAll(",", "")));
    double fhbcdje = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(3).children().get(3).text().replaceAll(",", "")));
    double drzqlj = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(4).children().get(1).text().replaceAll(",", "")));
    double hbcdje = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(4).children().get(3).text().replaceAll(",", "")));
    double commission = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(5).children().get(1).text().replaceAll(",", "")));
    double pledgeAmount = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(5).children().get(3).text().replaceAll(",", "")));
    double balanceCF = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(6).children().get(1).text().replaceAll(",", "")));
    double marginOccupied = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(6).children().get(3).text().replaceAll(",", "")));
    double fdyk = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(7).children().get(1).text().replaceAll(",", "")));
    double kyzj = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(7).children().get(3).text().replaceAll(",", "")));
    double riskDegree = 0d;
    double marginCall = 0d;
    try {
      riskDegree = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(8).children().get(1).text()).replaceAll("%", ""));
      marginCall = Double.parseDouble(StringUtils.trimAllWhitespace(accountElements.get(9).children().get(1).text().replaceAll(",", "")));

    } catch (Exception e) {

    }

    System.out.println(balanceBF + "-->" + clientEquity);
    System.out.println(depositWithdrawal + "-->" + khqy);
    System.out.println(realizedPL + "-->" + fhbcdje);
    System.out.println(drzqlj + "-->" + hbcdje);
    System.out.println(commission + "-->" + pledgeAmount);
    System.out.println(balanceCF + "-->" + marginOccupied);
    System.out.println(fdyk + "-->" + kyzj);
    System.out.println(riskDegree + "-->" + marginCall);

    Elements dealElements = elements.get(7).children().get(0).children();
    int size = dealElements.size();
    List<DealInfo> dealInfoList = new ArrayList<>();
    for (int i = 2; i < size-1; i++) {
      DealInfo dealInfo = new DealInfo();
      Elements datas = dealElements.get(i).children();
      dealInfo.setContract(datas.get(0).text());
      dealInfo.setDealType(datas.get(1).text());
      dealInfo.setSpeculateHedging(datas.get(2).text());
      if(!StringUtils.isEmpty(datas.get(3).text())) {
        dealInfo.setDealPrice(Double.parseDouble(datas.get(3).text().replaceAll(",", "")));
      }
      dealInfo.setBoardLot(Integer.parseInt(datas.get(4).text()));
      dealInfo.setDealFee(Double.parseDouble(datas.get(5).text().replaceAll(",", "")));
      dealInfo.setOpenClose(datas.get(6).text());
      dealInfo.setCommission(Double.parseDouble(datas.get(7).text()));
      dealInfo.setDealDate(date);
      try {
        dealInfo.setCloseProfit(Double.parseDouble(datas.get(8).text().replaceAll(",", "")));
      } catch (Exception e) {

      }
      dealInfoList.add(dealInfo);
    }

    dealInfoList.stream().forEach(deal -> {
      System.out.println(deal.getContract() + "->" + deal.getDealType() + "->" + deal.getSpeculateHedging() + "->" + deal.getDealPrice()
        + "->" + deal.getBoardLot() + "->" + deal.getDealFee() + "->" + deal.getOpenClose() + "->" + deal.getCommission() + "->" + deal.getCloseProfit());
    });

    Elements positionElements = elements.get(9).children().get(0).children();
    size = positionElements.size();
    List<PositionInfo> positionInfos = new ArrayList<>();
    for (int i = 2; i < size-1; i++) {
      PositionInfo positionInfo = new PositionInfo();
      Elements datas = positionElements.get(i).children();
      positionInfo.setPositionDay(date);
      positionInfo.setContract(datas.get(0).text());
      positionInfo.setBuyBoardLot(Integer.parseInt(datas.get(1).text()));
      positionInfo.setBuyPrice(Double.parseDouble(datas.get(2).text().replaceAll(",", "")));
      if(!StringUtils.isEmpty(datas.get(3).text())) {
        positionInfo.setSellBoardLot(Integer.parseInt(datas.get(3).text()));
      }
      if(!StringUtils.isEmpty(datas.get(4).text())) {
        positionInfo.setSellPrice(Double.parseDouble(datas.get(4).text().replaceAll(",", "")));
      }
      positionInfo.setYesterdayPrice(Double.parseDouble(datas.get(5).text().replaceAll(",", "")));
      positionInfo.setTodayPrice(Double.parseDouble(datas.get(6).text().replaceAll(",", "")));
      positionInfo.setProfit(Double.parseDouble(datas.get(7).text().replaceAll(",", "")));
      positionInfo.setDealMargin(Double.parseDouble(datas.get(8).text().replaceAll(",", "")));
      positionInfo.setSpeculateHedging(datas.get(9).text());
      positionInfos.add(positionInfo);
    }

    positionInfos.forEach(positionInfo -> {
      System.out.println(positionInfo.getContract());
    });
  }

  @Test
  public void getData() throws Exception {
    String url = "https://investorservice.cfmmc.com/customer/setupViewCustomerDetailFromCompanyAuto.do";

    String responseBody = HttpUtil.get(url, "6bXweWalWDl4eITCitb4BiJtbsByk7_Y-MFhQkQ330hX6akKFrvd!1999827751");
    responseBody = responseBody.replaceAll("&nbsp;", "");

    Document document = Jsoup.parse(responseBody);

    Elements token = document.getElementsByAttributeValue("name", "org.apache.struts.taglib.html.TOKEN");

    Element element = document.getElementById("waitBody");
    Elements elements = element.select("table.front-table");

    Elements clientInfoElements = elements.get(0).children().get(0).children();

    Elements lines = clientInfoElements.get(1).children();
    String clientId = StringUtils.trimAllWhitespace(lines.get(1).text());
    String data = StringUtils.trimAllWhitespace(lines.get(3).text());

    lines = clientInfoElements.get(2).children();
    String name = lines.get(1).text();

    lines = clientInfoElements.get(3).children();
    String company = lines.get(1).text();
    String account = lines.get(3).text();

    System.out.println(clientId);
    System.out.println(data);
    System.out.println(name);
    System.out.println(company);
    System.out.println(account);

  }
}
