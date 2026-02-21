package com.cvbuilder.servlet;

import com.cvbuilder.dao.DAOFactory;
import com.cvbuilder.model.CV;
import com.cvbuilder.model.User;
import com.cvbuilder.service.CVService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * SERVLET — DashboardServlet
 *
 * Charge tous les CVs de l'utilisateur connecté via CVService → CVDAO.
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Le Service récupère les CVs via le DAO
        CVService cvService = new CVService(
            DAOFactory.getCVDAO(getServletContext())
        );

        List<CV> cvList = cvService.getAllForUser(user.getUsername());

        req.setAttribute("cvList",  cvList);
        req.setAttribute("cvCount", cvList.size());
        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}
