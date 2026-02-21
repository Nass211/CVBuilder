package com.cvbuilder.servlet;

import com.cvbuilder.dao.DAOFactory;
import com.cvbuilder.model.CV;
import com.cvbuilder.model.User;
import com.cvbuilder.service.CVService;
import com.cvbuilder.service.CVService.CVFormParams;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * SERVLET — Step1Servlet : État Civil
 *
 * Flux DAO :
 *   Servlet → CVService.saveStep1() → CVDAO.save() → cv_ID.txt
 *
 * GET  → affiche le formulaire (avec données si édition)
 * POST → délègue au CVService → redirige vers step2
 */
@WebServlet("/cv/step1")
public class Step1Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = requireLogin(req, resp);
        if (session == null) return;

        String cvId = req.getParameter("id");

        if (cvId != null && !cvId.isEmpty()) {
            // Mode édition : charger le CV existant depuis le DAO
            User user = (User) session.getAttribute("user");
            CVService cvService = new CVService(DAOFactory.getCVDAO(getServletContext()));
            CV cv = cvService.findByIdForUser(cvId, user.getUsername());
            if (cv != null) {
                session.setAttribute("currentCV", cv);
                req.setAttribute("cv", cv);
            }
        } else {
            // Nouveau CV ou en cours → récupérer depuis session
            CV cv = (CV) session.getAttribute("currentCV");
            if (cv != null) req.setAttribute("cv", cv);
        }

        req.getRequestDispatcher("/WEB-INF/views/cv/step1.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = requireLogin(req, resp);
        if (session == null) return;

        User user = (User) session.getAttribute("user");
        CV existing = (CV) session.getAttribute("currentCV");

        // Construire le CVFormParams à partir des paramètres HTTP
        CVFormParams params = new CVFormParams();
        params.title        = req.getParameter("title");
        params.nom          = req.getParameter("nom");
        params.prenom       = req.getParameter("prenom");
        params.email        = req.getParameter("email");
        params.telephone    = req.getParameter("telephone");
        params.adresse      = req.getParameter("adresse");
        params.ville        = req.getParameter("ville");
        params.dateNaissance= req.getParameter("dateNaissance");
        params.nationalite  = req.getParameter("nationalite");
        params.linkedin     = req.getParameter("linkedin");
        params.siteWeb      = req.getParameter("siteWeb");
        params.resume       = req.getParameter("resume");

        // Déléguer au Service (qui appelle le DAO)
        CVService cvService = new CVService(DAOFactory.getCVDAO(getServletContext()));
        CV cv = cvService.saveStep1(existing, user.getUsername(), params);

        // Mettre le CV à jour dans la session
        session.setAttribute("currentCV", cv);

        resp.sendRedirect(req.getContextPath() + "/cv/step2");
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
