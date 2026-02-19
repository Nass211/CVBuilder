package com.cvbuilder.servlet;

import com.cvbuilder.model.User;
import com.cvbuilder.util.FileStorage;
import com.cvbuilder.util.StorageFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * CONTROLLER - Handles the Register page.
 *
 * GET  /register → shows the register form (register.jsp)
 * POST /register → processes the form, saves user, redirects to login
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Just show the registration form
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        // Basic validation
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {

            request.setAttribute("error", "Tous les champs sont obligatoires.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        if (username.trim().length() < 3) {
            request.setAttribute("error", "Le nom d'utilisateur doit avoir au moins 3 caractères.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        // Try to save the user
        FileStorage storage = StorageFactory.getStorage(getServletContext());
        User newUser = new User(username.trim(), password, email.trim());

        boolean saved = storage.saveUser(newUser);

        if (saved) {
            // Success! Redirect to login with a success message
            response.sendRedirect(request.getContextPath() + "/login?registered=true");
        } else {
            // Username already taken
            request.setAttribute("error", "Ce nom d'utilisateur est déjà pris. Choisissez-en un autre.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}
