package work.variety.trading.service;

import work.variety.trading.dto.CatchParameterDto;

/**
 * @author zhangbin
 * @date 2018/7/31 20:29
 */
public interface CatchDataService {

   boolean catchData(CatchParameterDto catchParameterDto) throws Exception;
}
