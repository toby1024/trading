package work.variety.trading.web;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import work.variety.trading.dto.AccountDto;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.SearchAccountDto;
import work.variety.trading.service.DayAccountSummaryService;
import work.variety.trading.service.ExportExcelService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author zhangbin
 * @date 2018/8/28 21:19
 */
@Controller
@RequestMapping("account")
public class AccountSummaryController {

  @GetMapping("")
  public String index(Model model, SearchAccountDto searchAccountDto) {
    setDefaultDate(searchAccountDto);
    PageDto<AccountDto> page = dayAccountSummaryService.search(searchAccountDto);
    model.addAttribute("page", page);
    model.addAttribute("searchCondition", searchAccountDto);

    return "account/index";
  }

  @GetMapping("{id}/show")
  public String show(Model model, @PathVariable("id") Integer id) {
    AccountDto accountDto = dayAccountSummaryService.detail(id);
    model.addAttribute("accountDto", accountDto);
    return "account/detail";
  }

  @GetMapping("stat")
  public String stat(Model model, SearchAccountDto searchAccountDto) {
    setDefaultDate(searchAccountDto);
    model.addAttribute("searchCondition", searchAccountDto);
    model.addAttribute("page", dayAccountSummaryService.statCommission(searchAccountDto));
    model.addAttribute("collectStatCommission", dayAccountSummaryService.collectStatCommission(searchAccountDto));

    Map<String, Object> barData = dayAccountSummaryService.collectStatCommissionBar(searchAccountDto);
    model.addAttribute("names", barData.get("names"));
    model.addAttribute("commissionData", barData.get("commissionData"));
    model.addAttribute("depositData", barData.get("depositData"));
    return "account/stat";
  }

  @GetMapping("excel")
  public ResponseEntity excel(SearchAccountDto searchAccountDto, HttpServletResponse response) throws IOException {
    setDefaultDate(searchAccountDto);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=手续费出入金.xls");
    String[] head = {"姓名", "手续费", "出入金"};
    String[] column = {"name", "commission", "depositWithdrawal"};

    String sheetName = DateFormatUtils.format(searchAccountDto.getStartDate(), "MM-dd") + "~"
      + DateFormatUtils.format(searchAccountDto.getEndDate(), "MM-dd") + "手续费出入金汇总";

    ByteArrayOutputStream outputStream = exportExcelService.exportExcel(
      dayAccountSummaryService.listStatCommission(searchAccountDto), column, head, sheetName);
    return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.CREATED);
  }

  private void setDefaultDate(SearchAccountDto searchAccountDto) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    if (searchAccountDto.getStartDate() == null) {
      Date startDate = DateUtils.addDays(new Date(), -30);
      try {
        searchAccountDto.setStartDate(sdf.parse(sdf.format(startDate)));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    if (searchAccountDto.getEndDate() == null) {
      Date endDate = new Date();
      try {
        searchAccountDto.setEndDate(sdf.parse(sdf.format(endDate)));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  }

  @Autowired
  private DayAccountSummaryService dayAccountSummaryService;
  @Autowired
  private ExportExcelService exportExcelService;

}
