package work.variety.trading.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import work.variety.trading.dto.AccountDto;
import work.variety.trading.dto.AccountStatDto;
import work.variety.trading.dto.SearchAccountDto;
import work.variety.trading.dto.SearchDayAccountDto;
import work.variety.trading.entity.DayAccountSummary;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin
 * @date 2018/8/1 15:04
 */
@Mapper
@Repository
public interface DayAccountSummaryMapper {

  int save(DayAccountSummary dayAccountSummary);

  DayAccountSummary find(SearchDayAccountDto searchDayAccountDto);

  int deleteByClientAndDate(@Param("clientInfoId") int clientInfoId, @Param("dealDate") Date dealDate);

  int count(SearchAccountDto searchAccountDto);

  List<AccountDto> search(SearchAccountDto searchAccountDto);

  AccountDto detail(@Param("id") Integer id);

  int countStatCommission(SearchAccountDto searchAccountDto);

  List<AccountStatDto> statCommission(SearchAccountDto searchAccountDto);

  AccountStatDto collectStatCommission(SearchAccountDto searchAccountDto);
}
