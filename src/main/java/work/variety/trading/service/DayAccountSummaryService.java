package work.variety.trading.service;

import work.variety.trading.dto.AccountDto;
import work.variety.trading.dto.AccountStatDto;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.SearchAccountDto;
import work.variety.trading.dto.SearchDayAccountDto;
import work.variety.trading.entity.DayAccountSummary;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin
 * @date 2018/8/1 15:37
 */
public interface DayAccountSummaryService {
  DayAccountSummary findOrCreate(DayAccountSummary dayAccountSummary);

  DayAccountSummary forceCreate(DayAccountSummary dayAccountSummary);

  int deleteByClientAndDate(int clientId, Date date);

  PageDto<AccountDto> search(SearchAccountDto searchAccountDto);

  List<Map> inOutChart(SearchAccountDto searchAccountDto, List<String> dates);

  List<AccountDto> seachList(SearchAccountDto searchAccountDto);

  AccountDto detail(Integer id);

  PageDto<AccountStatDto> statCommission(SearchAccountDto searchAccountDto);

  AccountStatDto collectStatCommission(SearchAccountDto searchAccountDto);

  Map<String, Object> collectStatCommissionBar(SearchAccountDto searchAccountDto);

  List<AccountStatDto> listStatCommission(SearchAccountDto searchAccountDto);

}
