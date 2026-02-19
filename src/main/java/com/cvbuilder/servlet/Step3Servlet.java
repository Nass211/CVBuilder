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
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLLER - Step 3: Formation & Loisirs.
 *
 * GET  /cv/step3 → show formation/loisirs form
 * POST /cv/step3 → save data, go to template selection
 */
@WebServlet("/cv/step3")
public class Step3Servlet extends HttpServlet {

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
        request.getRequestDispatcher("/WEB-INF/views/cv/step3.jsp").forward(request, response);
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

        // Process formations (same pattern as experiences)
        String[] diplomes = request.getParameterValues("diplome");
        String[] ecoles = request.getParameterValues("ecole");
        String[] annees = request.getParameterValues("annee");
        String[] descFormations = request.getParameterValues("descriptionForm");

        List<String> formations = new ArrayList<>();

        if (diplomes != null) {
            for (int i = 0; i < diplomes.length; i++) {
                String diplome = diplomes[i] != null ? diplomes[i].trim() : "";
                if (diplome.isEmpty()) continue;

                String ecole = get(ecoles, i);
                String annee = get(annees, i);
                String desc = get(descFormations, i);

                String formLine = clean(diplome) + "|" + clean(ecole) + "|"
                                + clean(annee) + "|" + clean(desc);
                formations.add(formLine);
            }
        }

        cv.setFormations(formations);
        cv.setLoisirs(request.getParameter("loisirs"));
        cv.setCompetences(request.getParameter("competences"));
        cv.setLangues(request.getParameter("langues"));

        session.setAttribute("currentCV", cv);

        // Save to file
        FileStorage storage = StorageFactory.getStorage(getServletContext());
        storage.saveCV(cv);

        // Go to template selection
        response.sendRedirect(request.getContextPath() + "/cv/template");
    }

    private String get(String[] arr, int i) {
        if (arr == null || i >= arr.length || arr[i] == null) return "";
        return arr[i].trim();
    }

    private String clean(String s) {
        return s == null ? "" : s.replace("|", " ").replace("\n", " ").replace("\r", "");
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
