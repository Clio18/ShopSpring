package com.luxcampus.web.filter;
import com.luxcampus.service.SecurityService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class SecurityFilter implements Filter {

    //list of allowed paths for filter
    private List<String> allowedPath = List.of("/login", "/registration");

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //getting bean for filter usage (cannot use autowired)
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletRequest.getServletContext());
        SecurityService securityService = (SecurityService) context.getBean("securityService");

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        for (String allowed : allowedPath) {
            if (httpServletRequest.getRequestURI().equals(allowed)){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        if (securityService.isAuth(httpServletRequest)){
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            httpServletResponse.sendRedirect("/login");
        }
    }

    @Override
    public void destroy() {

    }
}
