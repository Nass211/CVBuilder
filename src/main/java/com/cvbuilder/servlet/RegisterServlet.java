package com.cvbuilder.servlet;

import com.cvbuilder.dao.DAOFactory;
import com.cvbuilder.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * SERVLET — RegisterServlet
 *
 * Rôle dans l'architecture DAO :
 *   Servlet → UserService → UserDAO → FileSystemUserDAO → users.txt
 *
 * La Servlet ne fait que :
 *   1. Lire les paramètres HTTP
 *   2. Appeler le Service
 *   3. Gérer la réponse (redirect ou afficher erreur)
 *
 * Elle ne connaît PAS les fichiers, elle ne valide PAS les données.
 * Tout ça c'est le rôle du Service.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email    = req.getParameter("email");

        // Le Service gère la validation ET l'appel au DAO
        UserService userService = new UserService(
            DAOFactory.getUserDAO(getServletContext())
        );

        String error = userService.register(username, password, email);

        if (error == null) {
            // Succès → redirection vers login
            resp.sendRedirect(req.getContextPath() + "/login?registered=true");
        } else {
            // Erreur → retour au formulaire avec message
            req.setAttribute("error", error);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }
    }
}
