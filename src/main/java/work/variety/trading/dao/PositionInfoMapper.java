package work.variety.trading.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import work.variety.trading.dto.PositionInfoDto;
import work.variety.trading.dto.PositionStatDto;
import work.variety.trading.dto.SearchPositionDto;
import work.variety.trading.entity.PositionInfo;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin
 * @date 2018/7/26 09:50
 */
@Mapper
@Repository
public interface PositionInfoMapper {

  int save(PositionInfo positionInfo);

  PositionInfo findOne(PositionInfo positionInfo);

  int countStat(SearchPositionDto searchPositionDto);

  List<PositionStatDto> stat(SearchPositionDto searchPositionDto);

  int count(SearchPositionDto searchPositionDto);

  List<PositionInfoDto> search(SearchPositionDto searchPositionDto);

  int deleteByClientAndDate(@Param("clientId") int clientId, @Param("date") Date date);
}

