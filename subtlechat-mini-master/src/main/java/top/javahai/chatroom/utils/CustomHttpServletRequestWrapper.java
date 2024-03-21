package top.javahai.chatroom.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final String username;
    private final String password;

    public CustomHttpServletRequestWrapper(HttpServletRequest request, String username, String password) {
        super(request);
        this.username = username;
        this.password = password;
    }

    @Override
    public String getParameter(String name) {
        if ("username".equals(name)) {
            return username;
        } else if ("password".equals(name)) {
            return password;
        } else {
            return super.getParameter(name);
        }
    }
}