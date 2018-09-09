package work.variety.trading.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.stereotype.Service;
import work.variety.trading.service.ExportExcelService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangbin
 * @date 2018/9/7 14:56
 */
@Service
public class ExportExcelServiceImpl implements ExportExcelService {
  /**
   * 导出excel
   *
   * @param list   数据集合
   * @param column 列名
   * @param head   表头
   */
  @Override
  public ByteArrayOutputStream exportExcel(List list, String[] column, String[] head, String sheetName) {

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    // 声明一个工作薄
    HSSFWorkbook wb = new HSSFWorkbook();
    // 声明一个单子并命名
    HSSFSheet sheet = wb.createSheet(sheetName);
    // 给单子名称一个长度
    sheet.setDefaultColumnWidth((short) 15);
    // 生成一个样式
    HSSFCellStyle style = wb.createCellStyle();
    // 创建第一行（也可以称为表头）
    HSSFRow row = sheet.createRow(0);
    // 样式字体居中
    style.setAlignment(HorizontalAlignment.CENTER);
    // 给表头第一行一次创建单元格
    if (column == null || column.length == 0) {
      return null;
    }
    for (int index = 0; index < head.length; index++) {
      HSSFCell cell = row.createCell((short) index);
      // 名称..
      cell.setCellValue(head[index]);
      cell.setCellStyle(style);
    }

    Gson gson = new Gson();
    String data = gson.toJson(list);

    JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();

    int rowIndex = 1;
    for (JsonElement jsonElement : jsonArray) {
      HSSFRow rowData = sheet.createRow(rowIndex);
      rowIndex++;
      for (int index = 0; index < column.length; index++) {
        String result = jsonElement.getAsJsonObject().get(column[index]).toString();
        if (result != null) {
          rowData.createCell((short) index).setCellValue(result);
        }
      }
    }

    try {
      wb.write(outputStream);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        wb.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return outputStream;
  }
}
