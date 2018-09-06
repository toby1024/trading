package work.variety.trading.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import work.variety.trading.dto.AccountDto;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.SearchAccountDto;
import work.variety.trading.entity.ClientInfo;
import work.variety.trading.service.DayAccountSummaryService;

import java.util.List;

/**
 * @author zhangbin
 * @date 2018/8/28 21:19
 */
@Controller
@RequestMapping("account")
public class AccountSummaryController {

  @GetMapping("")
  public String index(Model model, SearchAccountDto searchAccountDto){

    PageDto<AccountDto> page = dayAccountSummaryService.search(searchAccountDto);
    model.addAttribute("page", page);
    model.addAttribute("searchCondition", searchAccountDto);

    return "account/index";
  }
  @GetMapping("{id}/show")
  public String show(Model model, @PathVariable("id") Integer id){
    AccountDto accountDto = dayAccountSummaryService.detail(id);
    model.addAttribute("accountDto", accountDto);
    return "account/detail";
  }

  @GetMapping("stat")
  public String stat(Model model, SearchAccountDto searchAccountDto){
    model.addAttribute("searchCondition", searchAccountDto);
    model.addAttribute("page", dayAccountSummaryService.statCommission(searchAccountDto));
    return "account/stat";
  }

  @Autowired
  private DayAccountSummaryService dayAccountSummaryService;

}
