package work.variety.trading.service;

import work.variety.trading.entity.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author zhangbin
 * @date 2018/8/3 13:03
 */
public interface UserService {

  User login(User user) throws NoSuchAlgorithmException;

  List<User> all();

  int delete(int id);

  User get(int id);

  User saveOrUpdate(User user);
}
