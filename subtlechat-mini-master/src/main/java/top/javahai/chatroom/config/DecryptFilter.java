package top.javahai.chatroom.config;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import top.javahai.chatroom.entity.RespBean;
import top.javahai.chatroom.utils.RSAUtil;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import top.javahai.chatroom.config.CustomHttpServletRequestWrapper;

@Component
public class DecryptFilter extends GenericFilterBean {
    private static final Set<String> ALLOWED_PATHS = new HashSet<>(Arrays.asList("/doLogin", "/admin/doLogin","/doLoginMail"));
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 检查请求路径是否需要解密
        if ("POST".equals(request.getMethod()) && ALLOWED_PATHS.contains(request.getServletPath())) {

            // 获取加密的用户名和密码
            String encryptedUsername = request.getParameter("username");
            String encryptedPassword = request.getParameter("password");
            System.out.println("Encrypted data is received, the data is "+encryptedUsername);
            // 解密用户名和密码
            String decryptedUsername = null;
            String decryptedPassword = null;


            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();

            try {
                decryptedUsername = RSAUtil.decryptWithPrivate(encryptedUsername);
                decryptedPassword = RSAUtil.decryptWithPrivate(encryptedPassword);
                System.out.println("The decrypted data username:"+decryptedUsername);
                System.out.println("The decrypted data password:"+decryptedPassword);
            } catch (Exception e) {
                writer.write(new ObjectMapper().writeValueAsString(RespBean.error("Decryption failed")));
            }
            CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(request, decryptedUsername, decryptedPassword);
            System.out.println(requestWrapper.getParameter("username"));
            System.out.println(requestWrapper.getParameter("password"));
            filterChain.doFilter(requestWrapper, response);
        } else {
            // 如果不需要解密，则直接传递给下一个过滤器
            filterChain.doFilter(request, response);
        }

    }
}