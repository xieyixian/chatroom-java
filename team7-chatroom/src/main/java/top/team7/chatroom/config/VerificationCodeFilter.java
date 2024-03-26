package top.team7.chatroom.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import top.team7.chatroom.entity.RespBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
public class VerificationCodeFilter extends GenericFilter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    //If it is a login request, intercept it
    if ("POST".equals(request.getMethod())&&"/doLogin".equals(request.getServletPath())){
      //Verification code entered by user

      String code = request.getParameter("code");
      String verify_code = (String) request.getSession().getAttribute("verify_code");
      System.out.println("code:"+code);

      response.setContentType("application/json;charset=utf-8");
      PrintWriter writer = response.getWriter();

      String type = request.getParameter("type");
      System.out.println(type);
      try {
        if (type.equals("mailVerify")){
          String mailCode = request.getParameter("mailCode");
          String verify_code_mail= (String) request.getSession().getAttribute("mail_verify_code_login");

          System.out.println("mailcode:"+mailCode);
          System.out.println("mailcode_login:"+verify_code_mail);

          try {
//        //Verify if they are the same
            if (!verify_code_mail.equals(mailCode)) {
              //Output json
              writer.write(new ObjectMapper().writeValueAsString( RespBean.error("Email verification code error！")));
            }else {
              System.out.println("successful verified");
              filterChain.doFilter(request,response);
            }
          }catch (NullPointerException e){
            writer.write(new ObjectMapper().writeValueAsString(RespBean.error("Request exception, please request again！")));
          }
        }else {
          if (!code.toLowerCase().equals(verify_code.toLowerCase())) {

            writer.write(new ObjectMapper().writeValueAsString(RespBean.error("Verification code error！")));
          } else {
            filterChain.doFilter(request, response);
          }
        }
      }catch (NullPointerException e){
        System.out.println("wrong");
        writer.write(new ObjectMapper().writeValueAsString(RespBean.error("Request exception, please request again！")));
      }finally {
        writer.flush();
        writer.close();
      }
    }
      //Management login request
    else if ("POST".equals(request.getMethod())&&"/admin/doLogin".equals(request.getServletPath())){
      //Get the entered verification code
      String mailCode = request.getParameter("mailCode");
      //Get the verification code saved in the session
      String verifyCode = ((String) request.getSession().getAttribute("mail_verify_code"));
      //Build response output stream
      response.setContentType("application/json;charset=utf-8");
      PrintWriter printWriter =response.getWriter();
      try {
        if(!mailCode.equals(verifyCode)){
          printWriter.write(new ObjectMapper().writeValueAsString(RespBean.error("Verification code error！")));
        }else {
          filterChain.doFilter(request,response);
        }
      }catch (NullPointerException e){
         printWriter.write(new ObjectMapper().writeValueAsString(RespBean.error("Request exception, please request again！")));
      }finally {
       printWriter.flush();
       printWriter.close();
      }
    }

    else {
      filterChain.doFilter(request,response);
    }
  }
}
