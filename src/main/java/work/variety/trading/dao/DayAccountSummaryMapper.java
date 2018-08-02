package work.variety.trading.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import work.variety.trading.dto.SearchDayAccountDto;
import work.variety.trading.entity.DayAccountSummary;

/**
 * @author zhangbin
 * @date 2018/8/1 15:04
 */
@Mapper
@Repository
public interface DayAccountSummaryMapper {

  int save(DayAccountSummary dayAccountSummary);

  DayAccountSummary find(SearchDayAccountDto searchDayAccountDto);
}
