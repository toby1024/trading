package work.variety.trading.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import work.variety.trading.dao.ClientInfoMapper;
import work.variety.trading.entity.ClientInfo;
import work.variety.trading.service.ClientInfoService;
import work.variety.trading.service.FileParseService;
import work.variety.trading.service.StorageProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author zhangbin
 * @date 2018/7/31 14:13
 */
@Service("txtDataParseService")
public class TxtDataParseService implements FileParseService {
  private Logger logger = LoggerFactory.getLogger(TxtDataParseService.class);

  private static final String DATE_PARSE_STR = "YYYYMMdd";
  private static final String DATETIME_PARSE_STR = "YYYY-MM-dd HH:mm:ss";

  private final Path rootLocation;

  @Autowired
  public TxtDataParseService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void parse(String filename) {
    File file = new File(this.rootLocation + "/" + filename);
    try {
      Date date = new Date();

      List<String> lines = FileUtils.readLines(file, "gbk");

      for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);
        System.out.println(line);
        ClientInfo clientInfo;
        // 交易结算单
        if (line.contains("交易结算单(盯市) Settlement Statement(MTM)")) {
          i++;
          line = lines.get(i);
          clientInfo = saveClientInfo(line);
          i++;
          String[] array = lines.get(i).split("：");
          date = DateUtils.parseDate(StringUtils.trimAllWhitespace(array[1]), DATE_PARSE_STR);
        }

        //资金情况
        if (line.contains("资金状况  币种：人民币  Account Summary  Currency：CNY")) {
          i += 2;
          // 未完成
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  private ClientInfo saveClientInfo(String line) {
    ClientInfo clientInfo = new ClientInfo();
    String[] array = line.split("客户名称 Client Name：");
    String[] clientIdArray = array[0].split("：");
    clientInfo.setFuturesCapitalNumber(StringUtils.trimAllWhitespace(clientIdArray[1]));
    clientInfo.setName(StringUtils.trimAllWhitespace(array[1]));
    return clientInfoService.createOrFind(clientInfo);
  }

  @Autowired
  private ClientInfoService clientInfoService;
}
