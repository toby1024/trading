package work.variety.trading.service;

import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.PositionInfoDto;
import work.variety.trading.dto.PositionStatDto;
import work.variety.trading.dto.SearchPositionDto;
import work.variety.trading.entity.PositionInfo;

/**
 * @author zhangbin
 * @date 2018/8/1 16:38
 */
public interface PositionInfoService {

  PositionInfo findOne(PositionInfo positionInfo);
  PositionInfo findOrCreate(PositionInfo positionInfo);

  PageDto<PositionStatDto> stat(SearchPositionDto searchPositionDto);

  PageDto<PositionInfoDto> search(SearchPositionDto searchPositionDto);
}
