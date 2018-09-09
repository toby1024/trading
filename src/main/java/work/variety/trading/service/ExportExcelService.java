package work.variety.trading.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * @author zhangbin
 * @date 2018/9/7 14:55
 */
public interface ExportExcelService {

  ByteArrayOutputStream exportExcel(List list, String[] column, String[] head, String sheetName);
  }
