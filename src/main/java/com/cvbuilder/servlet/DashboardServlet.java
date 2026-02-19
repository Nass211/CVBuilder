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
import java.util.List;

/**
 * CONTROLLER - Dashboard page.
 *
 * GET /dashboard → shows all CVs for the logged-in user
 *
 * This servlet checks if the user is logged in (via session).
 * If not, it redirects to login. This is called "authentication check".
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Not logged in → send to login page
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 2. Get the logged-in user from session
        User user = (User) session.getAttribute("user");

        // 3. Load all their CVs from files
        FileStorage storage = StorageFactory.getStorage(getServletContext());
        List<CV> userCVs = storage.getCVsForUser(user.getUsername());

        // 4. Put CVs into request so JSP can access them
        request.setAttribute("cvList", userCVs);
        request.setAttribute("cvCount", userCVs.size());

        // 5. Forward to dashboard view
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
