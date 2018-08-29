package work.variety.trading.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.dao.DayAccountSummaryMapper;
import work.variety.trading.dto.AccountDto;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.PositionStatDto;
import work.variety.trading.dto.SearchAccountDto;
import work.variety.trading.dto.SearchDayAccountDto;
import work.variety.trading.entity.DayAccountSummary;
import work.variety.trading.service.DayAccountSummaryService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    if (searchAccountDto.getStartDate() == null) {
      Date startDate = DateUtils.addDays(new Date(), -30);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      try {
        searchAccountDto.setStartDate(sdf.parse(sdf.format(startDate)));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    int count = accountDao.count(searchAccountDto);
    List<AccountDto> accounts = accountDao.search(searchAccountDto);
    return new PageDto<>(accounts, searchAccountDto.getPageNum(), count, searchAccountDto.getPageSize());
  }

  @Override
  public AccountDto detail(Integer id) {
    return accountDao.detail(id);
  }

  @Autowired
  private DayAccountSummaryMapper accountDao;
}
