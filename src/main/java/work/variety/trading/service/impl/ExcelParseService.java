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
import work.variety.trading.dao.ClientInfoMapper;
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
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

/**
 * @author zhangbin
 * @date 2018/7/25 13:32
 */
@Service
public class ExcelParseService implements FileParseService {
  private Logger logger = LoggerFactory.getLogger(ExcelParseService.class);

  private static final String DATE_PARSE_STR = "YYYY-MM-dd";
  private static final String DATETIME_PARSE_STR = "YYYY-MM-dd HH:mm:ss";

  private final Path rootLocation;

  @Autowired
  public ExcelParseService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void parse(String filename) {
    try {
      NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(this.rootLocation + "/" + filename));
      HSSFWorkbook wb = new HSSFWorkbook(fs.getRoot(), true);
      HSSFSheet sheet = wb.getSheet("持仓明细");
      // 基本信息
      ClientInfo clientInfo = saveClientInfo(sheet);
      Row row;
      int endRow = sheet.getLastRowNum();
      // 持仓明细
      for (int rowNumber = 10; rowNumber < endRow; rowNumber++) {
        row = sheet.getRow(rowNumber);
        savePositionInfo(row, clientInfo);
      }
      // 交易明细
      sheet = wb.getSheet("成交明细");
      endRow = sheet.getLastRowNum();
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

  private ClientInfo saveClientInfo(Sheet sheet) {
    Row row = sheet.getRow(2);
    ClientInfo clientInfo = new ClientInfo();
    clientInfo.setFuturesCapitalNumber(StringUtils.trimAllWhitespace(row.getCell(2).getStringCellValue()));
    row = sheet.getRow(3);
    clientInfo.setName(StringUtils.trimAllWhitespace(row.getCell(2).getStringCellValue()));
    row = sheet.getRow(4);
    clientInfo.setCompanyName(StringUtils.trimAllWhitespace(row.getCell(2).getStringCellValue()));
    clientInfo.setStockCapitalNumber(StringUtils.trimAllWhitespace(row.getCell(7).getStringCellValue()));
    clientInfoService.createOrFind(clientInfo);
    return clientInfo;
  }

  private void savePositionInfo(Row row, ClientInfo clientInfo) throws ParseException {
    PositionInfo positionInfo = new PositionInfo();
    positionInfo.setContract(StringUtils.trimAllWhitespace(row.getCell(0).getStringCellValue()));
    positionInfo.setDealNumber(StringUtils.trimAllWhitespace(row.getCell(1).getStringCellValue()));
    Cell cell = row.getCell(2);
    switch (cell.getCellTypeEnum()) {
      case NUMERIC:
        positionInfo.setBuyBoardLot((int) row.getCell(2).getNumericCellValue());
        break;
      default:
        //do nothing
    }

    cell = row.getCell(3);
    switch (cell.getCellTypeEnum()) {
      case NUMERIC:
        positionInfo.setBuyPrice(row.getCell(3).getNumericCellValue());
        break;
      default:
        //do nothing
    }

    cell = row.getCell(4);
    switch (cell.getCellTypeEnum()) {
      case NUMERIC:
        positionInfo.setSellBoardLot((int) row.getCell(4).getNumericCellValue());
        break;
      default:
        //do nothing
    }

    cell = row.getCell(5);
    switch (cell.getCellTypeEnum()) {
      case NUMERIC:
        positionInfo.setSellPrice(row.getCell(5).getNumericCellValue());
        break;
      default:
        //do nothing
    }

    positionInfo.setYesterdayPrice(row.getCell(6).getNumericCellValue());
    positionInfo.setTodayPrice(row.getCell(7).getNumericCellValue());
    positionInfo.setProfit(row.getCell(8).getNumericCellValue());
    positionInfo.setSpeculateHedging(StringUtils.trimAllWhitespace(row.getCell(9).getStringCellValue()));
    positionInfo.setTransactionNumber(StringUtils.trimAllWhitespace(row.getCell(10).getStringCellValue()));
    positionInfo.setRealDealDate(DateUtils.parseDate(row.getCell(11).getStringCellValue(), DATE_PARSE_STR));
    positionInfo.setClientInfoId(clientInfo.getId());
    positionInfoDao.save(positionInfo);
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
  private PositionInfoMapper positionInfoDao;
  @Autowired
  private DealInfoMapper dealInfoDao;
}
