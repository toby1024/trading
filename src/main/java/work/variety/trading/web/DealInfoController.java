package work.variety.trading.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import work.variety.trading.dto.DealInfoContractDto;
import work.variety.trading.dto.DealInfoDetailDto;
import work.variety.trading.dto.DealInfoDto;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.SearchDealInfoContractDto;
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

  @GetMapping("byContract")
  public String deailByContract(Model model, SearchDealInfoContractDto searchDealInfoContractDto) {
    PageDto<DealInfoContractDto> page = dealInfoService.searchByContract(searchDealInfoContractDto);
    model.addAttribute("page", page);
    model.addAttribute("searchCondition", searchDealInfoContractDto);
    return "detailByContract";
  }

  @GetMapping("detail")
  public String detail(Model model, SearchDealInfoContractDto searchDealInfoDto){
    PageDto<DealInfoDetailDto> page = dealInfoService.detail(searchDealInfoDto);
    model.addAttribute("page", page);
    model.addAttribute("searchCondition", searchDealInfoDto);
    return "detail";
  }

  @Autowired
  private DealInfoService dealInfoService;
}
