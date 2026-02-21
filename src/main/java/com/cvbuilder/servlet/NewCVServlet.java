package com.cvbuilder.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * SERVLET — NewCVServlet
 *
 * Efface le CV en cours de session pour démarrer un nouveau proprement.
 */
@WebServlet("/cv/new")
public class NewCVServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        session.removeAttribute("currentCV");
        resp.sendRedirect(req.getContextPath() + "/cv/step1");
    }
}
