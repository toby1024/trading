package work.variety.trading.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import work.variety.trading.entity.User;
import work.variety.trading.service.UserService;

import java.util.List;

/**
 * @author zhangbin
 * @date 2018/8/3 13:28
 */
@Controller
@RequestMapping("users")
public class UserController {

  @GetMapping("")
  public String index(Model model) {
    List<User> users = userService.all();
    model.addAttribute("users", users);
    return "user/index";
  }

  @GetMapping("new")
  public String newUser(Model model){
    User user = new User();
    model.addAttribute("user", user);
    return "user/userForm";
  }

  @GetMapping("edit")
  public String edit(int id, Model model) {
    User user = userService.get(id);
    model.addAttribute("user", user);
    return "user/userForm";
  }

  @PostMapping("")
  public String createOrUpdate(User user){
    userService.saveOrUpdate(user);
    return "redirect:/users";
  }

  @GetMapping("delete")
  public String delete(int id) {
    userService.delete(id);
    return "redirect:/users";
  }

  @Autowired
  private UserService userService;
}
