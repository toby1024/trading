package work.variety.trading.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.dao.DealInfoMapper;
import work.variety.trading.dto.DealInfoDto;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.SearchDealInfoDto;
import work.variety.trading.entity.DealInfo;
import work.variety.trading.service.DealInfoService;

import java.util.List;

/**
 * @author zhangbin
 * @date 2018/7/26 17:01
 */
@Service
public class DealInfoServiceImpl implements DealInfoService {

  @Override
  public List<DealInfo> list() {
    return dealInfoDao.list();
  }

  @Override
  public PageDto<DealInfoDto> search(SearchDealInfoDto searchDealInfoDto) {

    int totalCount = dealInfoDao.count(searchDealInfoDto);
    List<DealInfoDto> list = dealInfoDao.search(searchDealInfoDto);
    return new PageDto<DealInfoDto>(list, searchDealInfoDto.getPageNum(), totalCount, searchDealInfoDto.getPageSize());
  }

  @Autowired
  private DealInfoMapper dealInfoDao;
}
