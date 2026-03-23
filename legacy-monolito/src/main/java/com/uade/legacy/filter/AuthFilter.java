package com.uade.legacy.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri        = req.getRequestURI();
        String contextPath = req.getContextPath();

        boolean esRecursoPublico =
            uri.equals(contextPath + "/login")   ||
            uri.startsWith(contextPath + "/css/") ||
            uri.startsWith(contextPath + "/js/");

        if (esRecursoPublico) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean autenticado = session != null && session.getAttribute("usuarioLogueado") != null;

        if (autenticado) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(contextPath + "/login");
        }
    }

    @Override public void init(FilterConfig config) {}
    @Override public void destroy() {}
}
