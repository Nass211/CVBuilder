package com.cvbuilder.servlet;

import com.cvbuilder.dao.DAOFactory;
import com.cvbuilder.model.User;
import com.cvbuilder.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * SERVLET — LoginServlet
 *
 * GET  /login          → affiche le formulaire
 * POST /login          → appelle UserService.login() → crée session si OK
 * GET  /login?logout   → invalide la session
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Déconnexion
        if ("true".equals(req.getParameter("logout"))) {
            HttpSession session = req.getSession(false);
            if (session != null) session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login?loggedout=true");
            return;
        }

        // Déjà connecté → dashboard directement
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Appel au Service → qui appelle le DAO
        UserService userService = new UserService(
            DAOFactory.getUserDAO(getServletContext())
        );

        User user = userService.login(username, password);

        if (user != null) {
            // Connexion réussie → créer la session
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            req.setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
