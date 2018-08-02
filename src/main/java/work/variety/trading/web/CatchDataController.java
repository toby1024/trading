package work.variety.trading.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import work.variety.trading.dto.CatchParameterDto;
import work.variety.trading.service.CatchDataService;

/**
 * @author zhangbin
 * @date 2018/7/31 16:47
 */
@Controller
@RequestMapping("/catch")
public class CatchDataController {

  @GetMapping("")
  public String catchForm(){
    return "catchForm";
  }


  @PostMapping("")
  public String catchData(CatchParameterDto catchParameterDto) throws Exception {
    catchDataService.catchData(catchParameterDto);
    return "redirect:/catch";
  }
  @Autowired
  private CatchDataService catchDataService;
}
