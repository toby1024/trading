package work.variety.trading.service;

import work.variety.trading.entity.PositionInfo;

/**
 * @author zhangbin
 * @date 2018/8/1 16:38
 */
public interface PositionInfoService {

  PositionInfo findOne(PositionInfo positionInfo);
  PositionInfo findOrCreate(PositionInfo positionInfo);
}
