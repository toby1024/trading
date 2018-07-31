package work.variety.trading.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import work.variety.trading.dao.UserMapper;
import work.variety.trading.entity.User;
import work.variety.trading.util.HttpUtil;

import java.util.List;

/**
 * @author zhangbin
 * @date 2018/7/31 09:33
 */
@Component
public class KeepAliveJob {

  @Autowired
  private UserMapper userDao;

  private Logger logger = LoggerFactory.getLogger(KeepAliveJob.class);

//  @Scheduled(fixedDelay = 10 * 60 * 1000)
  public void keepAlive() {
    String url = "https://investorservice.cfmmc.com/customer/setupViewCustomerDetailFromCompanyAuto.do";
    List<User> users = userDao.all();
    users.stream().forEach(user -> {
      try {
        logger.info("保持用户："+user.getUsername()+" session存活");
//        HttpUtil.get(url, user.getJsessionId());
      } catch (Exception e) {
        logger.error("保存session存活出错", e);
      }
    });
  }
}
