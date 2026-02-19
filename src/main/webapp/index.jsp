<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // If logged in, go to dashboard; otherwise go to login
    if (session.getAttribute("user") != null) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
    } else {
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>
