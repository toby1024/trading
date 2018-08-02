package work.variety.trading.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.dao.DayAccountSummaryMapper;
import work.variety.trading.dto.SearchDayAccountDto;
import work.variety.trading.entity.DayAccountSummary;
import work.variety.trading.service.DayAccountSummaryService;

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

  @Autowired
  private DayAccountSummaryMapper accountDao;
}
