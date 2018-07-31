package work.variety.trading.service;

import work.variety.trading.dto.DealInfoContractDto;
import work.variety.trading.dto.DealInfoDetailDto;
import work.variety.trading.dto.DealInfoDto;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.SearchDealInfoContractDto;
import work.variety.trading.dto.SearchDealInfoDto;
import work.variety.trading.entity.DealInfo;

import java.util.List;

/**
 * @author zhangbin
 * @date 2018/7/26 17:01
 */
public interface DealInfoService {

  List<DealInfo> list();

  PageDto<DealInfoDto> search(SearchDealInfoDto searchDealInfoDto);

  PageDto<DealInfoContractDto> searchByContract(SearchDealInfoContractDto searchDealInfoContractDto);

  PageDto<DealInfoDetailDto> detail(SearchDealInfoDto searchDealInfoDto);
}
