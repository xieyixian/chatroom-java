package top.team7.chatroom.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import top.team7.chatroom.entity.RespBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Configuration
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception) throws IOException, ServletException {
    resp.setContentType("application/json;charset=utf-8");
    PrintWriter out=resp.getWriter();
    RespBean error = RespBean.error("Login failed!");
    if (exception instanceof LockedException){
      error.setMsg("The account has been locked, please contact the administrator!");
    }else if (exception instanceof CredentialsExpiredException){
      error.setMsg("The password has expired, please contact the administrator！");
    }else if (exception instanceof AccountExpiredException){
      error.setMsg("The account has expired, please contact the administrator！");
    }else if (exception instanceof DisabledException){
      error.setMsg("Account is disabled, please contact the administrator!");
    }else if (exception instanceof BadCredentialsException){
      error.setMsg("Username or password is wrong, please re-enter！");
    }
    String s = new ObjectMapper().writeValueAsString(error);
    out.write(s);
    out.flush();
    out.close();
  }
}
