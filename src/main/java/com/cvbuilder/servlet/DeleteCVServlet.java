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
 * CONTROLLER - Deletes a CV.
 * POST /cv/delete?id=xxx → deletes file, redirects to dashboard
 */
@WebServlet("/cv/delete")
public class DeleteCVServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String cvId = request.getParameter("id");
        if (cvId == null || cvId.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        FileStorage storage = StorageFactory.getStorage(getServletContext());
        CV cv = storage.loadCV(cvId);
        User user = (User) session.getAttribute("user");

        // Only delete if user owns this CV
        if (cv != null && cv.getOwnerUsername().equals(user.getUsername())) {
            storage.deleteCV(cvId);
        }

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
