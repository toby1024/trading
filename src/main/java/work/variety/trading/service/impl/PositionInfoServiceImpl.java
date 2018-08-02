package work.variety.trading.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.dao.PositionInfoMapper;
import work.variety.trading.entity.PositionInfo;
import work.variety.trading.service.PositionInfoService;

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

  @Autowired
  private PositionInfoMapper positionInfoDao;
}
