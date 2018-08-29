package work.variety.trading.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.dao.ClientInfoMapper;
import work.variety.trading.dao.PositionInfoMapper;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.PositionInfoDto;
import work.variety.trading.dto.PositionStatDto;
import work.variety.trading.dto.SearchPositionDto;
import work.variety.trading.entity.ClientInfo;
import work.variety.trading.entity.PositionInfo;
import work.variety.trading.service.PositionInfoService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangbin
 * @date 2018/8/1 16:42
 */
@Service
public class PositionInfoServiceImpl implements PositionInfoService {
  @Override
  public PositionInfo findOne(PositionInfo positionInfo) {
    return positionInfoDao.findOne(positionInfo);
  }

  @Override
  public PositionInfo findOrCreate(PositionInfo positionInfo) {
    PositionInfo info = positionInfoDao.findOne(positionInfo);
    if (info != null) {
      return info;
    }
    positionInfoDao.save(positionInfo);
    return positionInfo;
  }

  @Override
  public PositionInfo forceCreate(PositionInfo positionInfo) {
    positionInfoDao.save(positionInfo);
    return positionInfo;
  }

  @Override
  public PageDto<PositionStatDto> stat(SearchPositionDto searchPositionDto) {
    if (searchPositionDto.getStartDate() == null) {
      searchPositionDto.setStartDate(DateUtils.addDays(new Date(), -30));
    }
    int count = positionInfoDao.countStat(searchPositionDto);
    List<PositionStatDto> positions = positionInfoDao.stat(searchPositionDto);
    return new PageDto<PositionStatDto>(positions, searchPositionDto.getPageNum(), count, searchPositionDto.getPageSize());
  }

  @Override
  public PageDto<PositionInfoDto> search(SearchPositionDto searchPositionDto) {
    if (searchPositionDto.getStartDate() == null && searchPositionDto.getPositionDay() == null) {
      searchPositionDto.setStartDate(DateUtils.addDays(new Date(), -30));
    }
    int count = positionInfoDao.count(searchPositionDto);
    List<PositionInfoDto> list = positionInfoDao.search(searchPositionDto);
    return new PageDto<PositionInfoDto>(list, searchPositionDto.getPageNum(), count, searchPositionDto.getPageSize());
  }

  @Override
  public List<Map> lineChartData(SearchPositionDto searchPositionDto, List<String> dates) {
    searchPositionDto.setPage(false);
    List<Map> list = new ArrayList<>();

    List<ClientInfo> clientInfos = clientInfoDao.findByName(searchPositionDto.getName());
    clientInfos.forEach(clientInfo -> {
      Map<String, Object> map = new HashMap<>();
      map.put("name", clientInfo.getName());
      searchPositionDto.setClientInfoId(clientInfo.getId());

      List<PositionStatDto> positionStatDtos = positionInfoDao.stat(searchPositionDto);
      if (dates.size() == positionStatDtos.size()) {
        map.put("data", positionStatDtos.stream().map(PositionStatDto::getProfit).collect(Collectors.toList()).toArray());
      } else {
        List datas = new ArrayList(dates.size());
        for (int i = 0; i < dates.size(); i++) {
          try {
            Date day = DateUtils.parseDate(dates.get(i), "yy-MM-dd");
            boolean noData = true;
            for (int j = 0; j < positionStatDtos.size(); j++) {
              if (DateUtils.isSameDay(day, positionStatDtos.get(j).getPositionDay())) {
                datas.add(positionStatDtos.get(j).getProfit());
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
      map.put("stack", "浮动盈亏");
      list.add(map);
    });

    return list;
  }

  @Override
  public int deleteByClientAndDate(int clientId, Date date) {
    return positionInfoDao.deleteByClientAndDate(clientId, date);
  }

  @Autowired
  private PositionInfoMapper positionInfoDao;

  @Autowired
  private ClientInfoMapper clientInfoDao;
}
