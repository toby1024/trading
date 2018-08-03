package work.variety.trading.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import work.variety.trading.entity.ClientInfo;

import java.util.List;

/**
 * @author zhangbin
 * @date 2018/7/25 15:53
 */
@Mapper
@Repository
public interface ClientInfoMapper {

  @Insert("INSERT INTO client_info(futures_capital_number, name, company_name, stock_capital_number)" +
    "VALUES(#{futuresCapitalNumber},#{name},#{companyName},#{stockCapitalNumber})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int save(ClientInfo clientInfo);

  @Select("SELECT * FROM client_info WHERE futures_capital_number = #{futuresCapitalNumber}")
  ClientInfo findByFuturesCapitalNumber(@Param("futuresCapitalNumber") String futuresCapitalNumber);

  List<ClientInfo> findByName(@Param("name") String name);
}
