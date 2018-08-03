package work.variety.trading.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import work.variety.trading.entity.User;
import work.variety.trading.service.UserService;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

/**
 * @author zhangbin
 * @date 2018/7/25 10:05
 */
@Controller
@RequestMapping("")
public class IndexController {

  @RequestMapping("/index")
  public String index() {
    return "index";
  }

  @PostMapping("/login")
  public String login(User user, HttpSession session, Model model) {
    User u = null;
    try {
      u = userService.login(user);
    } catch (NoSuchAlgorithmException e) {
      model.addAttribute("message", "登录失败，用户名或密码错误");
      return "redirect:/";
    }
    if (u != null) {
      session.setAttribute("user", u);
      return "redirect:/position";
    } else {
      model.addAttribute("message", "登录失败，用户名或密码错误");
      return "redirect:/";
    }
  }

  @Autowired
  private UserService userService;
}
