package work.variety.trading.service;

import org.apache.poi.ss.usermodel.Sheet;
import work.variety.trading.entity.ClientInfo;

/**
 * @author zhangbin
 * @date 2018/7/26 11:02
 */
public interface ClientInfoService {

  ClientInfo createOrFind(ClientInfo clientInfo);

  ClientInfo saveClientInfo(Sheet sheet);
}
