package work.variety.trading.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import work.variety.trading.dto.DealInfoDto;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.SearchDealInfoDto;
import work.variety.trading.service.DealInfoService;

/**
 * @author zhangbin
 * @date 2018/7/26 17:02
 */
@Controller
@RequestMapping("/deal")
public class DealInfoController {

  @GetMapping("")
  public String index(Model model, SearchDealInfoDto searchDealInfoDto) {
    PageDto<DealInfoDto> page = dealInfoService.search(searchDealInfoDto);
    model.addAttribute("page", page);
    model.addAttribute("searchCondition", searchDealInfoDto);
    return "dealInfos";
  }

  @Autowired
  private DealInfoService dealInfoService;
}
