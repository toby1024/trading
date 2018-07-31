package work.variety.trading.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import work.variety.trading.dao.DealInfoMapper;
import work.variety.trading.dao.PositionInfoMapper;
import work.variety.trading.entity.ClientInfo;
import work.variety.trading.entity.DealInfo;
import work.variety.trading.entity.PositionInfo;
import work.variety.trading.exception.DataParseException;
import work.variety.trading.service.ClientInfoService;
import work.variety.trading.service.FileParseService;
import work.variety.trading.service.StorageProperties;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

/**
 * @author zhangbin
 * @date 2018/7/25 13:32
 */
@Service("monthExcelParseService")
public class MonthExcelParseService implements FileParseService {
  private Logger logger = LoggerFactory.getLogger(MonthExcelParseService.class);

  private static final String DATE_PARSE_STR = "YYYY-MM-dd";
  private static final String DATETIME_PARSE_STR = "YYYY-MM-dd HH:mm:ss";

  private final Path rootLocation;

  @Autowired
  public MonthExcelParseService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void parse(String filename) {
    try {
      NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(this.rootLocation + "/" + filename));
      HSSFWorkbook wb = new HSSFWorkbook(fs.getRoot(), true);
      HSSFSheet sheet = wb.getSheet("成交明细");
      // 基本信息
      ClientInfo clientInfo = clientInfoService.saveClientInfo(sheet);
      // 交易明细
      int endRow = sheet.getLastRowNum();
      Row row;
      // 持仓明细
      for (int rowNumber = 10; rowNumber < endRow; rowNumber++) {
        row = sheet.getRow(rowNumber);
        saveDealInfo(row, clientInfo);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new DataParseException("解析excel出错");
    }
  }



  private void saveDealInfo(Row row, ClientInfo clientInfo) throws ParseException {
    DealInfo dealInfo = new DealInfo();
    String date = row.getCell(0).getStringCellValue();
    String time = row.getCell(3).getStringCellValue();
    dealInfo.setDealDate(DateUtils.parseDate(date + " " + time, DATETIME_PARSE_STR));
    dealInfo.setContract(StringUtils.trimAllWhitespace(row.getCell(1).getStringCellValue()));
    dealInfo.setDealNumber(StringUtils.trimAllWhitespace(row.getCell(2).getStringCellValue()));
    dealInfo.setDealType(StringUtils.trimAllWhitespace(row.getCell(4).getStringCellValue()));
    dealInfo.setSpeculateHedging(StringUtils.trimAllWhitespace(row.getCell(5).getStringCellValue()));
    dealInfo.setDealPrice(row.getCell(6).getNumericCellValue());
    dealInfo.setBoardLot((int) row.getCell(7).getNumericCellValue());
    dealInfo.setDealFee(row.getCell(8).getNumericCellValue());
    dealInfo.setOpenClose(StringUtils.trimAllWhitespace(row.getCell(9).getStringCellValue()));
    dealInfo.setCommission(row.getCell(10).getNumericCellValue());

    Cell cell = row.getCell(11);
    switch (cell.getCellTypeEnum()) {
      case NUMERIC:
        dealInfo.setCloseProfit(row.getCell(11).getNumericCellValue());
        break;
      default:
        //do nothing
    }

    dealInfo.setRealDealDate(DateUtils.parseDate(row.getCell(12).getStringCellValue(),DATE_PARSE_STR));
    dealInfo.setClientInfoId(clientInfo.getId());
    dealInfoDao.save(dealInfo);
  }

  @Autowired
  private ClientInfoService clientInfoService;
  @Autowired
  private DealInfoMapper dealInfoDao;
}
