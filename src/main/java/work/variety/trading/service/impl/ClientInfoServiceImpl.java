package work.variety.trading.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.dao.ClientInfoMapper;
import work.variety.trading.entity.ClientInfo;
import work.variety.trading.service.ClientInfoService;

/**
 * @author zhangbin
 * @date 2018/7/26 11:03
 */
@Service
public class ClientInfoServiceImpl implements ClientInfoService {


  @Override
  public ClientInfo createOrFind(ClientInfo clientInfo) {
    ClientInfo info = clientInfoDao.findByFuturesCapitalNumber(clientInfo.getFuturesCapitalNumber());
    if(info == null){
      clientInfoDao.save(clientInfo);
      return clientInfo;
    }
    return info;
  }

  @Autowired
  private ClientInfoMapper clientInfoDao;
}
