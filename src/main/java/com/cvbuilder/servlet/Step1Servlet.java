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
import java.time.LocalDate;

/**
 * CONTROLLER - Step 1 of CV creation: État Civil (personal info).
 *
 * GET  /cv/step1          → show the form (new CV)
 * GET  /cv/step1?id=xxx   → show the form (editing existing CV)
 * POST /cv/step1          → save to session + file, go to step 2
 */
@WebServlet("/cv/step1")
public class Step1Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Auth check
        HttpSession session = requireLogin(request, response);
        if (session == null) return;

        String cvId = request.getParameter("id");

        if (cvId != null && !cvId.isEmpty()) {
            // Editing existing CV → load it from file
            FileStorage storage = StorageFactory.getStorage(getServletContext());
            CV cv = storage.loadCV(cvId);
            if (cv != null) {
                // Put CV in session so we can build on it across steps
                session.setAttribute("currentCV", cv);
                request.setAttribute("cv", cv);
            }
        } else {
            // New CV → check if there's already one in progress in session
            CV cv = (CV) session.getAttribute("currentCV");
            if (cv != null) {
                request.setAttribute("cv", cv);
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/cv/step1.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Auth check
        HttpSession session = requireLogin(request, response);
        if (session == null) return;

        User user = (User) session.getAttribute("user");

        // Get or create CV from session
        CV cv = (CV) session.getAttribute("currentCV");
        if (cv == null) {
            cv = new CV();
            cv.setOwnerUsername(user.getUsername());
            cv.setCreatedAt(LocalDate.now().toString());
        }

        // Fill in the personal info from the form
        cv.setTitle(request.getParameter("title"));
        cv.setNom(request.getParameter("nom"));
        cv.setPrenom(request.getParameter("prenom"));
        cv.setEmail(request.getParameter("email"));
        cv.setTelephone(request.getParameter("telephone"));
        cv.setAdresse(request.getParameter("adresse"));
        cv.setVille(request.getParameter("ville"));
        cv.setDateNaissance(request.getParameter("dateNaissance"));
        cv.setNationalite(request.getParameter("nationalite"));
        cv.setLinkedin(request.getParameter("linkedin"));
        cv.setSiteWeb(request.getParameter("siteWeb"));
        cv.setResume(request.getParameter("resume"));

        // Save to session (so next steps can access it)
        session.setAttribute("currentCV", cv);

        // Also save to file (in case user closes browser, data isn't lost)
        FileStorage storage = StorageFactory.getStorage(getServletContext());
        storage.saveCV(cv);

        // Go to step 2
        response.sendRedirect(request.getContextPath() + "/cv/step2");
    }

    // Helper: checks if user is logged in, redirects if not
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
