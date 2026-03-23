package com.uade.legacy.servlet;

import com.uade.legacy.dao.UsuarioDAO;
import com.uade.legacy.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            resp.sendRedirect(req.getContextPath() + "/productos");
            return;
        }
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Usuario y contraseña son obligatorios.");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }

        Usuario usuario = usuarioDAO.autenticar(username.trim(), password);

        if (usuario != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("usuarioLogueado", usuario);
            session.setMaxInactiveInterval(30 * 60);
            resp.sendRedirect(req.getContextPath() + "/productos");
        } else {
            req.setAttribute("error", "Usuario o contraseña incorrectos.");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        }
    }
}
