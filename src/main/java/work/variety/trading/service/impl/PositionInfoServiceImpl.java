package work.variety.trading.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.dao.PositionInfoMapper;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.PositionInfoDto;
import work.variety.trading.dto.PositionStatDto;
import work.variety.trading.dto.SearchPositionDto;
import work.variety.trading.entity.PositionInfo;
import work.variety.trading.service.PositionInfoService;

import java.util.Date;
import java.util.List;

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

  @Autowired
  private PositionInfoMapper positionInfoDao;
}
