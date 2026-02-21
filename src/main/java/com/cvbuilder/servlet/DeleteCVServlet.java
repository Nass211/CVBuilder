package com.cvbuilder.servlet;

import com.cvbuilder.dao.DAOFactory;
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
 * SERVLET — DeleteCVServlet
 *
 * POST → CVService.delete() → vérifie propriété → CVDAO.delete() → supprime le fichier
 */
@WebServlet("/cv/delete")
public class DeleteCVServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String cvId = req.getParameter("id");
        User user   = (User) session.getAttribute("user");

        if (cvId != null && !cvId.isEmpty()) {
            // Le Service vérifie la propriété AVANT de supprimer
            CVService cvService = new CVService(DAOFactory.getCVDAO(getServletContext()));
            cvService.delete(cvId, user.getUsername());
        }

        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}
