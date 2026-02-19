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
 * CONTROLLER - Step 2: Expériences Professionnelles.
 *
 * GET  /cv/step2 → show experience form
 * POST /cv/step2 → save experiences to session + file, go to step 3
 *
 * How experiences are stored:
 * Each experience is ONE string: "poste|entreprise|dateDebut|dateFin|description"
 * We separate them with | so we can split them later in JSP.
 */
@WebServlet("/cv/step2")
public class Step2Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = requireLogin(request, response);
        if (session == null) return;

        // Make sure they came from step 1
        CV cv = (CV) session.getAttribute("currentCV");
        if (cv == null) {
            response.sendRedirect(request.getContextPath() + "/cv/step1");
            return;
        }

        request.setAttribute("cv", cv);
        request.getRequestDispatcher("/WEB-INF/views/cv/step2.jsp").forward(request, response);
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

        // The form can have multiple experiences.
        // Each field is an array: poste[], entreprise[], etc.
        String[] postes = request.getParameterValues("poste");
        String[] entreprises = request.getParameterValues("entreprise");
        String[] dateDebuts = request.getParameterValues("dateDebut");
        String[] dateFins = request.getParameterValues("dateFin");
        String[] descriptions = request.getParameterValues("descriptionExp");

        List<String> experiences = new ArrayList<>();

        if (postes != null) {
            for (int i = 0; i < postes.length; i++) {
                String poste = postes[i] != null ? postes[i].trim() : "";
                if (poste.isEmpty()) continue; // Skip empty entries

                String entreprise = get(entreprises, i);
                String dateDebut = get(dateDebuts, i);
                String dateFin = get(dateFins, i);
                String desc = get(descriptions, i);

                // Combine into one string separated by | (pipe character)
                // We replace | in the values to avoid parsing issues
                String expLine = clean(poste) + "|" + clean(entreprise) + "|"
                               + clean(dateDebut) + "|" + clean(dateFin) + "|" + clean(desc);
                experiences.add(expLine);
            }
        }

        cv.setExperiences(experiences);
        session.setAttribute("currentCV", cv);

        // Save to file
        FileStorage storage = StorageFactory.getStorage(getServletContext());
        storage.saveCV(cv);

        // Go to step 3
        response.sendRedirect(request.getContextPath() + "/cv/step3");
    }

    // Helper: safely get array element (avoids ArrayIndexOutOfBoundsException)
    private String get(String[] arr, int i) {
        if (arr == null || i >= arr.length || arr[i] == null) return "";
        return arr[i].trim();
    }

    // Helper: remove pipe characters from values so our format doesn't break
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
