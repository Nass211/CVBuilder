package com.cvbuilder.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CONTROLLER - Starts a brand-new CV creation.
 *
 * GET /cv/new → clears any CV in progress from session → redirects to step 1
 *
 * Without this, if someone clicks "Create CV" while already having one in progress,
 * they'd continue the old one. This gives them a clean slate.
 */
@WebServlet("/cv/new")
public class NewCVServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Remove the CV in progress from session → clean start
        session.removeAttribute("currentCV");

        // Go to step 1
        response.sendRedirect(request.getContextPath() + "/cv/step1");
    }
}
