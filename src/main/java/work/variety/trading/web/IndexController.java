package work.variety.trading.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhangbin
 * @date 2018/7/25 10:05
 */
@Controller
@RequestMapping("/index")
public class IndexController {

  @RequestMapping("")
  public String index() {
    return "index";
  }
}
