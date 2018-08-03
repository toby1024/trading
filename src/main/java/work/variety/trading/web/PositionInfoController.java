package work.variety.trading.web;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    searchPositionDto.setOrderBy("client_info_id, position_day");

    if (searchPositionDto.getStartDate() == null) {
      searchPositionDto.setStartDate(DateUtils.addDays(new Date(), -30));
    }
    PageDto<PositionStatDto> page = positionInfoService.stat(searchPositionDto);
    model.addAttribute("page", page);
    model.addAttribute("searchCondition", searchPositionDto);

    List dates = parseDate(searchPositionDto);
    model.addAttribute("dates", dates);

    List<Map> lineData = positionInfoService.lineChartData(searchPositionDto, dates);
    model.addAttribute("lineData", lineData);
    model.addAttribute("names", lineData.stream().map(map->{return map.get("name");}).collect(Collectors.toList()));

    return "position/positionStat";
  }

  @GetMapping("detail")
  public String detail(Model model, SearchPositionDto searchPositionDto) {
    PageDto<PositionInfoDto> page = positionInfoService.search(searchPositionDto);
    model.addAttribute("page", page);
    model.addAttribute("searchCondition", searchPositionDto);
    return "position/positionDetail";
  }

  private List parseDate(SearchPositionDto searchPositionDto) {
    List<String> list = new ArrayList<>();
    Date date = searchPositionDto.getStartDate();
    Date endDate = searchPositionDto.getEndDate() == null ? new Date() : searchPositionDto.getEndDate();
    while (!date.after(endDate)) {
      list.add(DateFormatUtils.format(date, "yy-MM-dd"));
      date = DateUtils.addDays(date, 1);
    }
    return list;
  }

  @Autowired
  private PositionInfoService positionInfoService;
}
