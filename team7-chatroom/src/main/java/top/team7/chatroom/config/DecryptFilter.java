package top.team7.chatroom.config;

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
import top.team7.chatroom.utils.CustomHttpServletRequestWrapper;
import top.team7.chatroom.utils.RSAUtil;
import top.team7.chatroom.entity.RespBean;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Component
public class DecryptFilter extends GenericFilterBean {
    private static final Set<String> ALLOWED_PATHS = new HashSet<>(Arrays.asList("/doLogin", "/admin/doLogin","/doLoginMail"));
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Check whether the request path needs to be decrypted
        if ("POST".equals(request.getMethod()) && ALLOWED_PATHS.contains(request.getServletPath())) {

            // Get encrypted username and password
            String encryptedUsername = request.getParameter("username");
            String encryptedPassword = request.getParameter("password");
            System.out.println("Encrypted data is received, the data is "+encryptedUsername);
            // Decrypt username and password
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
            // If decryption is not required, pass it directly to the next filter
            filterChain.doFilter(request, response);
        }

    }
}