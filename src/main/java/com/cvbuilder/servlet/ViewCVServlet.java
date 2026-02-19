package com.cvbuilder.servlet;

import com.cvbuilder.model.CV;
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
 * CONTROLLER - View a CV.
 *
 * GET /cv/view?id=xxx → displays the CV with chosen template
 *
 * Based on the template choice, it forwards to the right JSP.
 */
@WebServlet("/cv/view")
public class ViewCVServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = requireLogin(request, response);
        if (session == null) return;

        String cvId = request.getParameter("id");
        if (cvId == null || cvId.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        FileStorage storage = StorageFactory.getStorage(getServletContext());
        CV cv = storage.loadCV(cvId);

        if (cv == null) {
            request.setAttribute("error", "CV introuvable.");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Make sure the logged-in user owns this CV
        User user = (User) session.getAttribute("user");
        if (!cv.getOwnerUsername().equals(user.getUsername())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.setAttribute("cv", cv);

        // Forward to the appropriate template view
        int template = cv.getTemplateChoice();
        String templateView;
        switch (template) {
            case 2: templateView = "/WEB-INF/views/cv/templates/template2.jsp"; break;
            case 3: templateView = "/WEB-INF/views/cv/templates/template3.jsp"; break;
            default: templateView = "/WEB-INF/views/cv/templates/template1.jsp"; break;
        }

        request.getRequestDispatcher(templateView).forward(request, response);
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
