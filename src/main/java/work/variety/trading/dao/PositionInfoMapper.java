package work.variety.trading.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import work.variety.trading.entity.PositionInfo;

/**
 * @author zhangbin
 * @date 2018/7/26 09:50
 */
@Mapper
@Repository
public interface PositionInfoMapper {

  @Insert("INSERT INTO position_info(contract, deal_number, buy_board_lot, buy_price, sell_board_lot, sell_price, yesterday_price, today_price, profit, speculate_hedging, transaction_number, real_deal_date)" +
          "VALUES(#{contract}, #{dealNumber}, #{buyBoardLot}, #{buyPrice}, #{sellBoardLot}, #{sellPrice}, #{yesterdayPrice}, #{todayPrice}, #{profit}, #{speculateHedging}, #{transactionNumber}, #{realDealDate})")
  int save(PositionInfo positionInfo);
}
