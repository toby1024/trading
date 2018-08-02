package work.variety.trading.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import work.variety.trading.dto.DealInfoContractDto;
import work.variety.trading.dto.DealInfoDetailDto;
import work.variety.trading.dto.DealInfoDto;
import work.variety.trading.dto.SearchDealInfoContractDto;
import work.variety.trading.dto.SearchDealInfoDto;
import work.variety.trading.entity.DealInfo;

import java.util.List;

/**
 * @author zhangbin
 * @date 2018/7/26 14:35
 */
@Mapper
@Repository
public interface DealInfoMapper {
  @Insert("INSERT INTO deal_info (deal_date, contract, deal_number, deal_type, speculate_hedging, deal_price, board_lot, deal_fee, open_close, commission, close_profit, real_deal_date, client_info_id)" +
    "VALUES(#{dealDate}, #{contract}, #{dealNumber}, #{dealType}, #{speculateHedging}, #{dealPrice}, #{boardLot}, #{dealFee}, #{openClose}, #{commission}, #{closeProfit}, #{realDealDate}, #{clientInfoId})")
  int save(DealInfo dealInfo);

  List<DealInfo> list();

  List<DealInfoDto> collect(SearchDealInfoDto searchDealInfoDto);

  int collectCount(SearchDealInfoDto searchDealInfoDto);

  List<DealInfoContractDto> collectByContract(SearchDealInfoContractDto searchDealInfoContractDto);

  int countByContract(SearchDealInfoContractDto searchDealInfoContractDto);

  int count(SearchDealInfoDto searchDealInfoDto);

  List<DealInfoDetailDto> detail(SearchDealInfoDto searchDealInfoDto);

  DealInfo findOne(DealInfo dealInfo);
}
