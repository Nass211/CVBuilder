package com.cvbuilder.servlet;

import com.cvbuilder.model.User;
import com.cvbuilder.util.FileStorage;
import com.cvbuilder.util.StorageFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CONTROLLER - Handles Login and Logout.
 *
 * GET  /login          → shows login form
 * POST /login          → authenticates user, creates session
 * GET  /login?logout=true → destroys session, redirects to login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Handle logout
        if ("true".equals(request.getParameter("logout"))) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // Destroy the session
            }
            response.sendRedirect(request.getContextPath() + "/login?loggedout=true");
            return;
        }

        // If user is already logged in, go to dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Show login form
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            request.setAttribute("error", "Veuillez remplir tous les champs.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        FileStorage storage = StorageFactory.getStorage(getServletContext());
        User user = storage.authenticate(username.trim(), password);

        if (user != null) {
            // Login success! Create a session and store the user in it
            HttpSession session = request.getSession(true); // Create new session
            session.setAttribute("user", user);            // Store user object
            session.setMaxInactiveInterval(30 * 60);       // Session expires after 30 minutes

            // Redirect to dashboard
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            // Wrong credentials
            request.setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
