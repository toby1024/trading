package work.variety.trading.service;

import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.PositionInfoDto;
import work.variety.trading.dto.PositionStatDto;
import work.variety.trading.dto.SearchPositionDto;
import work.variety.trading.entity.PositionInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin
 * @date 2018/8/1 16:38
 */
public interface PositionInfoService {

  PositionInfo findOne(PositionInfo positionInfo);

  PositionInfo findOrCreate(PositionInfo positionInfo);

  PositionInfo forceCreate(PositionInfo positionInfo);

  PageDto<PositionStatDto> stat(SearchPositionDto searchPositionDto);

  PageDto<PositionInfoDto> search(SearchPositionDto searchPositionDto);

  List<Map> lineChartData(SearchPositionDto searchPositionDto, List<String> dates);

  int deleteByClientAndDate(int clientId, Date date);
}
