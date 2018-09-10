package work.variety.trading.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.dao.ClientInfoMapper;
import work.variety.trading.dao.DayAccountSummaryMapper;
import work.variety.trading.dto.AccountDto;
import work.variety.trading.dto.AccountStatDto;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.PositionStatDto;
import work.variety.trading.dto.SearchAccountDto;
import work.variety.trading.dto.SearchDayAccountDto;
import work.variety.trading.entity.ClientInfo;
import work.variety.trading.entity.DayAccountSummary;
import work.variety.trading.service.DayAccountSummaryService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    int count = accountDao.count(searchAccountDto);
    List<AccountDto> accounts = accountDao.search(searchAccountDto);
    return new PageDto<>(accounts, searchAccountDto.getPageNum(), count, searchAccountDto.getPageSize());
  }

  @Override
  public List<Map> inOutChart(SearchAccountDto searchAccountDto, List<String> dates) {
    searchAccountDto.setPage(false);
    searchAccountDto.setOrderBy("a.accountDay");
    searchAccountDto.setOrderDesc("asc");

    List<Map> list = new ArrayList<>();
    Integer oldClientInfoId = searchAccountDto.getClientInfoId();

    List<ClientInfo> clientInfos = clientInfoDao.findById(oldClientInfoId);
    clientInfos.forEach(clientInfo -> {
      Map<String, Object> map = new HashMap<>();
      map.put("name", clientInfo.getName());
      searchAccountDto.setClientInfoId(clientInfo.getId());
      List<AccountDto> accounts = accountDao.search(searchAccountDto);
      if (dates.size() == accounts.size()) {
        map.put("data", accounts.stream().map(AccountDto::getZjsly).collect(Collectors.toList()).toArray());
      } else {
        List datas = new ArrayList(dates.size());
        for (int i = 0; i < dates.size(); i++) {
          try {
            Date day = DateUtils.parseDate(dates.get(i), "yy-MM-dd");
            boolean noData = true;
            for (int j = 0; j < accounts.size(); j++) {
              if (DateUtils.isSameDay(day, accounts.get(j).getAccountDay())) {
                datas.add(accounts.get(j).getZjsly());
                noData = false;
                break;
              }
            }
            if (noData) {
              datas.add(0);
            }

          } catch (ParseException e) {
            e.printStackTrace();
          }
        }
        map.put("data", datas);
      }
      map.put("type", "line");
      map.put("stack", "资金使用率（%）");
      list.add(map);
    });


    searchAccountDto.setClientInfoId(oldClientInfoId);
    return list;
  }

  @Override
  public List<AccountDto> seachList(SearchAccountDto searchAccountDto) {
    searchAccountDto.setPage(false);
    return accountDao.search(searchAccountDto);
  }

  @Override
  public AccountDto detail(Integer id) {
    return accountDao.detail(id);
  }

  @Override
  public PageDto<AccountStatDto> statCommission(SearchAccountDto searchAccountDto) {
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
    List<AccountStatDto> list = listStatCommission(searchAccountDto);

    Map<String, Object> result = new HashMap<>(2);
    result.put("names", list.stream().map(accountStatDto -> accountStatDto.getName()).collect(Collectors.toList()));
    result.put("commissionData", list.stream().map(accountStatDto -> accountStatDto.getCommission()).collect(Collectors.toList()));
    result.put("depositData", list.stream().map(accountStatDto -> accountStatDto.getDepositWithdrawal()).collect(Collectors.toList()));
    return result;
  }

  @Override
  public List<AccountStatDto> listStatCommission(SearchAccountDto searchAccountDto) {
    searchAccountDto.setPage(false);
    List<AccountStatDto> list = accountDao.statCommission(searchAccountDto);
    return list;
  }

  @Autowired
  private DayAccountSummaryMapper accountDao;
  @Autowired
  private ClientInfoMapper clientInfoDao;
}
