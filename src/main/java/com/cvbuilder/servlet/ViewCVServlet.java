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

/**
 * SERVLET — ViewCVServlet
 *
 * Charge le CV via CVService (qui passe par CVDAO),
 * puis forward vers le bon template JSP selon le choix de design.
 */
@WebServlet("/cv/view")
public class ViewCVServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String cvId = req.getParameter("id");
        if (cvId == null || cvId.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        User user = (User) session.getAttribute("user");

        // CVService vérifie la propriété et charge via CVDAO
        CVService cvService = new CVService(DAOFactory.getCVDAO(getServletContext()));
        CV cv = cvService.findByIdForUser(cvId, user.getUsername());

        if (cv == null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        req.setAttribute("cv", cv);

        // Choisir le template JSP selon le choix de l'utilisateur
        String templateView;
        switch (cv.getTemplateChoice()) {
            case 2:  templateView = "/WEB-INF/views/cv/templates/template2.jsp"; break;
            case 3:  templateView = "/WEB-INF/views/cv/templates/template3.jsp"; break;
            default: templateView = "/WEB-INF/views/cv/templates/template1.jsp"; break;
        }

        req.getRequestDispatcher(templateView).forward(req, resp);
    }
}
