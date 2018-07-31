package work.variety.trading.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.dao.DealInfoMapper;
import work.variety.trading.dto.DealInfoContractDto;
import work.variety.trading.dto.DealInfoDetailDto;
import work.variety.trading.dto.DealInfoDto;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.SearchDealInfoContractDto;
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

    int totalCount = dealInfoDao.collectCount(searchDealInfoDto);
    List<DealInfoDto> list = dealInfoDao.collect(searchDealInfoDto);
    return new PageDto<DealInfoDto>(list, searchDealInfoDto.getPageNum(), totalCount, searchDealInfoDto.getPageSize());
  }

  @Override
  public PageDto<DealInfoContractDto> searchByContract(SearchDealInfoContractDto searchDealInfoContractDto) {
    int totalCount = dealInfoDao.countByContract(searchDealInfoContractDto);
    List<DealInfoContractDto> list = dealInfoDao.collectByContract(searchDealInfoContractDto);
    return new PageDto<DealInfoContractDto>(list, searchDealInfoContractDto.getPageNum(), totalCount, searchDealInfoContractDto.getPageSize());
  }

  @Override
  public PageDto<DealInfoDetailDto> detail(SearchDealInfoDto searchDealInfoDto) {
    int totalCount = dealInfoDao.count(searchDealInfoDto);
    List<DealInfoDetailDto> list = dealInfoDao.detail(searchDealInfoDto);
    return new PageDto<DealInfoDetailDto>(list, searchDealInfoDto.getPageNum(), totalCount, searchDealInfoDto.getPageSize());
  }

  @Autowired
  private DealInfoMapper dealInfoDao;
}
