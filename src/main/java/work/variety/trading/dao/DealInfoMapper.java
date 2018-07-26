package work.variety.trading.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import work.variety.trading.entity.DealInfo;

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
}
