package work.variety.trading.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import work.variety.trading.entity.User;

import java.util.List;

/**
 * @author zhangbin
 * @date 2018/7/31 10:09
 */
@Mapper
@Repository
public interface UserMapper {

  List<User> all();
}
