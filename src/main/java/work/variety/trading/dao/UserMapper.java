package work.variety.trading.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

  @Select("SELECT * FROM user where username = #{username}")
  User findByUsername(@Param("username") String username);

  @Update("update user set state = 0 where id = #{id}")
  int delete(int id);

  @Select("SELECT * FROM user where id = #{id}")
  User get(int id);

  @Update("update user set password = #{password} where id = #{id}")
  int updatePass(User user);

  int create(User user);
}
