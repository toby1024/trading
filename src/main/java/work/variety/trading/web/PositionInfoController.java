package work.variety.trading.web;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import work.variety.trading.dto.PageDto;
import work.variety.trading.dto.PositionInfoDto;
import work.variety.trading.dto.PositionStatDto;
import work.variety.trading.dto.SearchPositionDto;
import work.variety.trading.service.PositionInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangbin
 * @date 2018/8/2 09:27
 */
@Controller
@RequestMapping("position")
public class PositionInfoController {

  @GetMapping("")
  public String index(Model model, SearchPositionDto searchPositionDto) {
    PageDto<PositionStatDto> page = positionInfoService.stat(searchPositionDto);
    model.addAttribute("page", page);
    model.addAttribute("searchCondition", searchPositionDto);
    List<PositionStatDto> list = page.getList();
    List days = new ArrayList();
    List datas = new ArrayList();
    list.forEach(dto -> {
      days.add(DateFormatUtils.format(dto.getPositionDay(), "yyMMdd"));
      datas.add(dto.getProfit());
    });
    model.addAttribute("days", days);
    model.addAttribute("datas", datas);
    return "position/positionStat";
  }

  @GetMapping("detail")
  public String detail(Model model, SearchPositionDto searchPositionDto) {
    PageDto<PositionInfoDto> page = positionInfoService.search(searchPositionDto);
    model.addAttribute("page", page);
    model.addAttribute("searchCondition", searchPositionDto);
    return "position/positionDetail";
  }

  @Autowired
  private PositionInfoService positionInfoService;
}
