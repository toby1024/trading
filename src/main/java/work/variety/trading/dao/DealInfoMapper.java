package work.variety.trading.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import work.variety.trading.dto.DealInfoContractDto;
import work.variety.trading.dto.DealInfoDetailDto;
import work.variety.trading.dto.DealInfoDto;
import work.variety.trading.dto.SearchDealInfoContractDto;
import work.variety.trading.dto.SearchDealInfoDto;
import work.variety.trading.entity.DealInfo;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin
 * @date 2018/7/26 14:35
 */
@Mapper
@Repository
public interface DealInfoMapper {
  int save(DealInfo dealInfo);

  List<DealInfo> list();

  List<DealInfoDto> collect(SearchDealInfoDto searchDealInfoDto);

  int collectCount(SearchDealInfoDto searchDealInfoDto);

  List<DealInfoContractDto> collectByContract(SearchDealInfoContractDto searchDealInfoContractDto);

  int countByContract(SearchDealInfoContractDto searchDealInfoContractDto);

  int count(SearchDealInfoDto searchDealInfoDto);

  List<DealInfoDetailDto> detail(SearchDealInfoDto searchDealInfoDto);

  DealInfo findOne(DealInfo dealInfo);

  int deleteByClientAndDate(@Param("clientInfoId") int clientInfoId, @Param("dealDate") Date dealDate);
}
