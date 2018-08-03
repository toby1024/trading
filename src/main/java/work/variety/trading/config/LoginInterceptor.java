package work.variety.trading.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author zhangbin
 * @date 2018/8/3 13:13
 */
@Configuration
public class LoginInterceptor implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new MyInterceptor()).excludePathPatterns("/", "/index", "/login", "/**/*.css", "/**/*.js");
  }

  class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
      HttpSession session = request.getSession();
      if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/");
        return false;
      }
      return true;
    }
  }
}
