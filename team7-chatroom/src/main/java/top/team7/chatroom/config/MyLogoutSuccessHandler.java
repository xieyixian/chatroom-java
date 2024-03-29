package top.team7.chatroom.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import top.team7.chatroom.entity.RespBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Configuration
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

  @Override
    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
      resp.setContentType("application/json;charset=utf-8");
      PrintWriter out=resp.getWriter();
      out.write(new ObjectMapper().writeValueAsString(RespBean.ok("Exit successfully！")));
      out.flush();
      out.close();
  }
}
