package work.variety.trading.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import work.variety.trading.dto.PositionStatDto;
import work.variety.trading.dto.SearchPositionDto;
import work.variety.trading.entity.PositionInfo;

import java.util.List;

/**
 * @author zhangbin
 * @date 2018/7/26 09:50
 */
@Mapper
@Repository
public interface PositionInfoMapper {

  int save(PositionInfo positionInfo);

  List<PositionStatDto> stat(SearchPositionDto searchPositionDto);

  PositionInfo findOne(PositionInfo positionInfo);
}
