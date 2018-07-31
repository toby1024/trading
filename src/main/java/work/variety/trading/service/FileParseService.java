package work.variety.trading.service;

import org.apache.poi.ss.usermodel.Sheet;
import work.variety.trading.entity.ClientInfo;

/**
 * @author zhangbin
 * @date 2018/7/25 13:31
 */
public interface FileParseService {


  void parse(String filename);
}
