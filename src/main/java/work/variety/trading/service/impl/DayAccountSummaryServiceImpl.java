package work.variety.trading.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.dao.DayAccountSummaryMapper;
import work.variety.trading.dto.AccountDto;
import work.variety.trading.dto.AccountStatDto;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.PositionStatDto;
import work.variety.trading.dto.SearchAccountDto;
import work.variety.trading.dto.SearchDayAccountDto;
import work.variety.trading.entity.DayAccountSummary;
import work.variety.trading.service.DayAccountSummaryService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangbin
 * @date 2018/8/1 15:37
 */
@Service
public class DayAccountSummaryServiceImpl implements DayAccountSummaryService {
  @Override
  public DayAccountSummary findOrCreate(DayAccountSummary dayAccountSummary) {
    SearchDayAccountDto searchDayAccountDto = new SearchDayAccountDto();
    searchDayAccountDto.setAccountDay(dayAccountSummary.getAccountDay());
    searchDayAccountDto.setClientInfoId(dayAccountSummary.getClientInfoId());
    DayAccountSummary account = accountDao.find(searchDayAccountDto);
    if (account != null) {
      return account;
    }
    accountDao.save(dayAccountSummary);
    return dayAccountSummary;
  }

  @Override
  public DayAccountSummary forceCreate(DayAccountSummary dayAccountSummary) {
    accountDao.save(dayAccountSummary);
    return dayAccountSummary;
  }

  @Override
  public int deleteByClientAndDate(int clientId, Date date) {
    return accountDao.deleteByClientAndDate(clientId, date);
  }

  @Override
  public PageDto<AccountDto> search(SearchAccountDto searchAccountDto) {

    searchAccountDto.setOrderBy("a.clientInfoId, a.accountDay");
    searchAccountDto.setOrderDesc("desc");

    setDefaultDate(searchAccountDto);

    int count = accountDao.count(searchAccountDto);
    List<AccountDto> accounts = accountDao.search(searchAccountDto);
    return new PageDto<>(accounts, searchAccountDto.getPageNum(), count, searchAccountDto.getPageSize());
  }

  @Override
  public AccountDto detail(Integer id) {
    return accountDao.detail(id);
  }

  @Override
  public PageDto<AccountStatDto> statCommission(SearchAccountDto searchAccountDto) {
    setDefaultDate(searchAccountDto);

    searchAccountDto.setOrderBy("clientInfoId");
    searchAccountDto.setOrderDesc("desc");

    int count = accountDao.countStatCommission(searchAccountDto);
    List<AccountStatDto> list = accountDao.statCommission(searchAccountDto);
    return new PageDto<>(list, searchAccountDto.getPageNum(), count, searchAccountDto.getPageSize());
  }

  @Override
  public AccountStatDto collectStatCommission(SearchAccountDto searchAccountDto) {
    return accountDao.collectStatCommission(searchAccountDto);
  }

  @Override
  public Map<String, Object> collectStatCommissionBar(SearchAccountDto searchAccountDto) {
    searchAccountDto.setPage(false);
    List<AccountStatDto> list = accountDao.statCommission(searchAccountDto);
    Map<String, Object> result = new HashMap<>(2);
    result.put("names",list.stream().map(accountStatDto -> accountStatDto.getName()).collect(Collectors.toList()));
    result.put("commissionData",list.stream().map(accountStatDto -> accountStatDto.getCommission()).collect(Collectors.toList()));
    result.put("depositData",list.stream().map(accountStatDto -> accountStatDto.getDepositWithdrawal()).collect(Collectors.toList()));
    return result;
  }

  private void setDefaultDate(SearchAccountDto searchAccountDto){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    if (searchAccountDto.getStartDate() == null) {
      Date startDate = DateUtils.addDays(new Date(), -30);
      try {
        searchAccountDto.setStartDate(sdf.parse(sdf.format(startDate)));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    if (searchAccountDto.getEndDate() == null) {
      Date endDate = new Date();
      try {
        searchAccountDto.setEndDate(sdf.parse(sdf.format(endDate)));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  }

  @Autowired
  private DayAccountSummaryMapper accountDao;
}
