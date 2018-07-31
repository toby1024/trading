package work.variety.trading.service.impl;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

  @Override
  public ClientInfo saveClientInfo(Sheet sheet) {
    Row row = sheet.getRow(2);
    ClientInfo clientInfo = new ClientInfo();
    clientInfo.setFuturesCapitalNumber(StringUtils.trimAllWhitespace(row.getCell(2).getStringCellValue()));
    row = sheet.getRow(3);
    clientInfo.setName(StringUtils.trimAllWhitespace(row.getCell(2).getStringCellValue()));
    row = sheet.getRow(4);
    clientInfo.setCompanyName(StringUtils.trimAllWhitespace(row.getCell(2).getStringCellValue()));
    clientInfo.setStockCapitalNumber(StringUtils.trimAllWhitespace(row.getCell(7).getStringCellValue()));
    clientInfo = createOrFind(clientInfo);
    return clientInfo;
  }

  @Autowired
  private ClientInfoMapper clientInfoDao;
}
