package com.cvbuilder.servlet;

import com.cvbuilder.dao.DAOFactory;
import com.cvbuilder.model.CV;
import com.cvbuilder.service.CVService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * SERVLET — Step3Servlet : Formation & Loisirs
 *
 * POST → CVService.saveStep3() → CVDAO.save() → cv_ID.txt
 */
@WebServlet("/cv/step3")
public class Step3Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = requireLogin(req, resp);
        if (session == null) return;

        CV cv = (CV) session.getAttribute("currentCV");
        if (cv == null) {
            resp.sendRedirect(req.getContextPath() + "/cv/step1");
            return;
        }

        req.setAttribute("cv", cv);
        req.getRequestDispatcher("/WEB-INF/views/cv/step3.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = requireLogin(req, resp);
        if (session == null) return;

        CV cv = (CV) session.getAttribute("currentCV");
        if (cv == null) {
            resp.sendRedirect(req.getContextPath() + "/cv/step1");
            return;
        }

        // Déléguer au Service
        CVService cvService = new CVService(DAOFactory.getCVDAO(getServletContext()));
        cv = cvService.saveStep3(
            cv,
            req.getParameterValues("diplome"),
            req.getParameterValues("ecole"),
            req.getParameterValues("annee"),
            req.getParameterValues("descriptionForm"),
            req.getParameter("loisirs"),
            req.getParameter("competences"),
            req.getParameter("langues")
        );

        session.setAttribute("currentCV", cv);
        resp.sendRedirect(req.getContextPath() + "/cv/template");
    }

    private HttpSession requireLogin(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        return session;
    }
}
