package com.cvbuilder.servlet;

import com.cvbuilder.model.CV;
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
 * CONTROLLER - Template selection page.
 *
 * GET  /cv/template → show 3 template previews to pick from
 * POST /cv/template → save template choice, show final CV
 */
@WebServlet("/cv/template")
public class TemplateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = requireLogin(request, response);
        if (session == null) return;

        CV cv = (CV) session.getAttribute("currentCV");
        if (cv == null) {
            response.sendRedirect(request.getContextPath() + "/cv/step1");
            return;
        }

        request.setAttribute("cv", cv);
        request.getRequestDispatcher("/WEB-INF/views/cv/template.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = requireLogin(request, response);
        if (session == null) return;

        CV cv = (CV) session.getAttribute("currentCV");
        if (cv == null) {
            response.sendRedirect(request.getContextPath() + "/cv/step1");
            return;
        }

        // Save template choice (1, 2, or 3)
        String templateParam = request.getParameter("template");
        int template = 1;
        try {
            template = Integer.parseInt(templateParam);
            if (template < 1 || template > 3) template = 1;
        } catch (NumberFormatException e) {
            template = 1;
        }

        cv.setTemplateChoice(template);
        session.setAttribute("currentCV", cv);

        // Save final CV to file
        FileStorage storage = StorageFactory.getStorage(getServletContext());
        storage.saveCV(cv);

        // Go to view/preview page
        response.sendRedirect(request.getContextPath() + "/cv/view?id=" + cv.getId());
    }

    private HttpSession requireLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return session;
    }
}
