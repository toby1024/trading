package work.variety.trading.service;

import work.variety.trading.entity.DayAccountSummary;

/**
 * @author zhangbin
 * @date 2018/8/1 15:37
 */
public interface DayAccountSummaryService {
  DayAccountSummary findOrCreate(DayAccountSummary dayAccountSummary);
}
