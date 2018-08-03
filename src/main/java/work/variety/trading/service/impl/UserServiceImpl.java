package work.variety.trading.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.dao.UserMapper;
import work.variety.trading.entity.User;
import work.variety.trading.service.UserService;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author zhangbin
 * @date 2018/8/3 13:04
 */
@Service
public class UserServiceImpl implements UserService {
  @Override
  public User login(User user) throws NoSuchAlgorithmException {
    User u = userDao.findByUsername(user.getUsername());
    if (u.getPassword().equals(getMD5(user.getPassword()))) {
      return u;
    }
    return null;
  }

  @Override
  public List<User> all() {
    return userDao.all();
  }

  @Override
  public int delete(int id) {
    return userDao.delete(id);
  }

  @Override
  public User get(int id) {
    return userDao.get(id);
  }

  @Override
  public User saveOrUpdate(User user) {
    try {
      user.setPassword(getMD5(user.getPassword()));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    if(user.getId() == null){
      user.setState(1);
      userDao.create(user);
    }else{
      userDao.updatePass(user);
    }

    return user;
  }

  private String getMD5(String str) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(str.getBytes());
    return new BigInteger(1, md.digest()).toString(16);
  }

  @Autowired
  private UserMapper userDao;
}
