package top.team7.chatroom.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import top.team7.chatroom.entity.Admin;
import top.team7.chatroom.utils.RSAUtil;
import top.team7.chatroom.entity.RespBean;
import top.team7.chatroom.entity.User;
import top.team7.chatroom.service.impl.AdminServiceImpl;
import top.team7.chatroom.service.impl.UserServiceImpl;
import top.team7.chatroom.utils.JwtUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import static top.team7.chatroom.constant.RedisKeyConstant.USER_CONTINUE_LIFE_KEY;
import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
public class MultiHttpSecurityConfig {

  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }
  private static void configureSwagger(HttpSecurity http) throws Exception {
    // Swagger
    http.authorizeRequests()
            .antMatchers("/swagger-ui/**", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/csrf", "/").permitAll();
  }
  @Configuration
  @Order(1)
  public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    AdminServiceImpl adminService;
    @Autowired
    VerificationCodeFilter verificationCodeFilter;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    MyLogoutSuccessHandler myLogoutSuccessHandler;
    @Autowired
    DecryptFilter decryptFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(adminService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
      web.ignoring().antMatchers("/css/**","/fonts/**","/img/**","/js/**","/favicon.ico","/index.html","/admin/login","/admin/mailVerifyCode","/getPublicKey","/user/register","/getAESKey");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.addFilterBefore(decryptFilter,UsernamePasswordAuthenticationFilter.class);
      http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
      configureSwagger(http);
      http.antMatcher("/admin/**").authorizeRequests()
              .anyRequest().authenticated()
              .and()
              .formLogin()
              .usernameParameter("username")
              .passwordParameter("password")
              .loginPage("/admin/login")
              .loginProcessingUrl("/admin/doLogin")
              .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                  resp.setContentType("application/json;charset=utf-8");
                  PrintWriter out = resp.getWriter();
                  Admin admin = (Admin) authentication.getPrincipal();
                  admin.setPassword(null);
                  RSAUtil rsa = new RSAUtil();

                  String encryptedusername = null;
                  String encrypteduseremail = null;
                  try {
                    encryptedusername = rsa.encryptWithPrivate(admin.getUsername());
                    encrypteduseremail = rsa.encryptWithPrivate(admin.getEmail());
                  } catch (Exception e) {
                    RespBean wrong = RespBean.ok("fail");
                    String w = new ObjectMapper().writeValueAsString(wrong);
                    out.write(w);
                    out.flush();
                    out.close();
                  }

                  admin.setUsername(encryptedusername);
                  admin.setEmail(encrypteduseremail);

                  RespBean ok = RespBean.ok("success", admin);
                  String s = new ObjectMapper().writeValueAsString(ok);
                  out.write(s);
                  out.flush();
                  out.close();
                }
              })

              .failureHandler(myAuthenticationFailureHandler)
              .permitAll()
              .and()
              .logout()
              .logoutUrl("/admin/logout")
              .logoutSuccessHandler(myLogoutSuccessHandler)
              .permitAll()
              .and()
              .csrf().disable()
              .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
                @Override
                public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                  httpServletResponse.setStatus(401);
                }
              });
    }
}






  @Configuration
  @Order(2)
  public static class UserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    VerificationCodeFilter verificationCodeFilter;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    DecryptFilter decryptFilter;

    @Resource
    private RedissonClient redisson;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
      web.ignoring().antMatchers("/login","/verifyCode","/file","/ossFileUpload","/user/register","/user/checkUsername","/user/checkNickname","/getPublicKey","/user/loginMailVerifyCode","/user/mailVerifyCode","/doLoginMail","/getAESKey");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      configureSwagger(http);
      http.addFilterBefore(decryptFilter,UsernamePasswordAuthenticationFilter.class);
      http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
      http.authorizeRequests()
              .anyRequest().authenticated()
              .and()
              .formLogin()
              .usernameParameter("username")
              .passwordParameter("password")
              .loginPage("/login")
              .loginProcessingUrl("/doLogin")
              .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                  resp.setContentType("application/json;charset=utf-8");
                  PrintWriter out=resp.getWriter();
                  User user=(User) authentication.getPrincipal();
                  user.setPassword(null);

                  userService.setUserStateToOn(user.getId());
                  user.setUserStateId(1);

                  RBucket<Object> bucket = redisson.getBucket(String.format(USER_CONTINUE_LIFE_KEY,user.getId()));
                  bucket.set("user_"+user.getId(),8L, TimeUnit.SECONDS);

                  simpMessagingTemplate.convertAndSend("/topic/notification","System：user【"+user.getNickname()+"】coming chat room");

                  final String jwt = JwtUtils.generateToken(user.getUsername());

                  Map<String, Object> tokenResponse = new HashMap<>();
                  tokenResponse.put("token", jwt);
                  tokenResponse.put("user", user);

                  String responseJson = new ObjectMapper().writeValueAsString(RespBean.ok("success", tokenResponse));

                  out.write(responseJson);
                  out.flush();
                  out.close();
                }
              })
              .failureHandler(myAuthenticationFailureHandler)
              .permitAll()
              .and()
              .logout()
              .logoutUrl("/logout")
              .logoutSuccessHandler(new LogoutSuccessHandler() {
                @Override
                public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                  User user = (User) authentication.getPrincipal();
                  userService.setUserStateToLeave(user.getId());

                  RBucket<Object> bucket = redisson.getBucket(String.format(USER_CONTINUE_LIFE_KEY,user.getId()));
                  bucket.set("user_"+user.getId(),0L, TimeUnit.SECONDS);

                  simpMessagingTemplate.convertAndSend("/topic/notification","system：user【"+user.getNickname()+"】logout chatroom");
                  resp.setContentType("application/json;charset=utf-8");
                  PrintWriter out=resp.getWriter();
                  out.write(new ObjectMapper().writeValueAsString(RespBean.ok("logout！")));
                  out.flush();
                  out.close();
                }
              })
              .permitAll()
              .and()
              .csrf().disable()
              .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
                @Override
                public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                  httpServletResponse.setStatus(401);
                }
              });
    }
  }

}
