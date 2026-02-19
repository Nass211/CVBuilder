<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cvbuilder.model.CV, com.cvbuilder.model.User, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    List<CV> cvList = (List<CV>) request.getAttribute("cvList");
    int cvCount = (int) request.getAttribute("cvCount");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard – CV Builder</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
        * { font-family: 'Inter', sans-serif; }
        .cv-card:hover { transform: translateY(-2px); }
        .cv-card { transition: all 0.2s ease; }
    </style>
</head>
<body class="min-h-screen bg-slate-50">

    <!-- Navbar -->
    <nav class="bg-white border-b border-slate-200 sticky top-0 z-10">
        <div class="max-w-6xl mx-auto px-6 py-4 flex items-center justify-between">
            <div class="flex items-center gap-3">
                <div class="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center">
                    <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                    </svg>
                </div>
                <span class="font-bold text-slate-800 text-lg">CV Builder</span>
            </div>

            <div class="flex items-center gap-4">
                <span class="text-slate-500 text-sm">Bonjour, <strong class="text-slate-700"><%= user.getUsername() %></strong></span>
                <a href="<%= request.getContextPath() %>/login?logout=true"
                   class="text-sm text-slate-500 hover:text-red-500 transition-colors">
                    Déconnexion
                </a>
            </div>
        </div>
    </nav>

    <!-- Main content -->
    <div class="max-w-6xl mx-auto px-6 py-10">

        <!-- Header row -->
        <div class="flex items-center justify-between mb-8">
            <div>
                <h1 class="text-2xl font-bold text-slate-800">Mes CVs</h1>
                <p class="text-slate-500 text-sm mt-1">
                    <%= cvCount == 0 ? "Vous n'avez pas encore créé de CV." : cvCount + " CV" + (cvCount > 1 ? "s" : "") + " créé" + (cvCount > 1 ? "s" : "") %>
                </p>
            </div>
            <a href="<%= request.getContextPath() %>/cv/new"
               class="inline-flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold
                      px-5 py-3 rounded-xl transition-colors duration-200 text-sm">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
                </svg>
                Créer un CV
            </a>
        </div>

        <!-- CV Grid -->
        <% if (cvCount == 0) { %>
        <!-- Empty state -->
        <div class="text-center py-20">
            <div class="w-20 h-20 bg-slate-100 rounded-2xl flex items-center justify-center mx-auto mb-5">
                <svg class="w-10 h-10 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                          d="M9 13h6m-3-3v6m-9 1V7a2 2 0 012-2h6l2 2h6a2 2 0 012 2v8a2 2 0 01-2 2H5a2 2 0 01-2-2z"/>
                </svg>
            </div>
            <h3 class="text-lg font-semibold text-slate-600 mb-2">Aucun CV pour l'instant</h3>
            <p class="text-slate-400 text-sm mb-6">Créez votre premier CV en quelques minutes !</p>
            <a href="<%= request.getContextPath() %>/cv/new"
               class="inline-flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold
                      px-6 py-3 rounded-xl transition-colors duration-200">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
                </svg>
                Créer mon premier CV
            </a>
        </div>

        <% } else { %>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
            <% for (CV cv : cvList) {
                String[] templateLabels = {"", "Bleu Classique", "Teal Moderne", "Violet Créatif"};
                String[] templateColors = {"", "bg-blue-100 text-blue-700", "bg-teal-100 text-teal-700", "bg-violet-100 text-violet-700"};
                int tpl = cv.getTemplateChoice();
                if (tpl < 1 || tpl > 3) tpl = 1;
            %>
            <div class="cv-card bg-white rounded-2xl border border-slate-200 overflow-hidden">
                <!-- CV card top color bar -->
                <div class="h-2 <%= tpl == 1 ? "bg-blue-500" : tpl == 2 ? "bg-teal-500" : "bg-violet-500" %>"></div>

                <div class="p-5">
                    <!-- Name & title -->
                    <div class="mb-4">
                        <h3 class="font-semibold text-slate-800 text-base truncate">
                            <%= cv.getTitle() != null && !cv.getTitle().isEmpty() ? cv.getTitle() : "Sans titre" %>
                        </h3>
                        <p class="text-slate-500 text-sm mt-0.5"><%= cv.getFullName() %></p>
                    </div>

                    <!-- Meta info -->
                    <div class="flex items-center gap-3 mb-5">
                        <span class="text-xs px-2.5 py-1 rounded-lg font-medium <%= templateColors[tpl] %>">
                            <%= templateLabels[tpl] %>
                        </span>
                        <% if (cv.getCreatedAt() != null && !cv.getCreatedAt().isEmpty()) { %>
                        <span class="text-xs text-slate-400"><%= cv.getCreatedAt() %></span>
                        <% } %>
                    </div>

                    <!-- Actions -->
                    <div class="flex items-center gap-2">
                        <a href="<%= request.getContextPath() %>/cv/view?id=<%= cv.getId() %>"
                           class="flex-1 text-center bg-slate-100 hover:bg-slate-200 text-slate-700
                                  text-sm font-medium py-2.5 rounded-xl transition-colors">
                            Voir
                        </a>
                        <a href="<%= request.getContextPath() %>/cv/step1?id=<%= cv.getId() %>"
                           class="flex-1 text-center bg-slate-100 hover:bg-slate-200 text-slate-700
                                  text-sm font-medium py-2.5 rounded-xl transition-colors">
                            Modifier
                        </a>
                        <a href="<%= request.getContextPath() %>/cv/download?id=<%= cv.getId() %>"
                           class="bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium
                                  py-2.5 px-3 rounded-xl transition-colors">
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"/>
                            </svg>
                        </a>
                        <form action="<%= request.getContextPath() %>/cv/delete" method="POST" class="inline"
                              onsubmit="return confirm('Supprimer ce CV ?')">
                            <input type="hidden" name="id" value="<%= cv.getId() %>">
                            <button type="submit"
                                    class="bg-red-50 hover:bg-red-100 text-red-500 text-sm font-medium
                                           py-2.5 px-3 rounded-xl transition-colors">
                                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                          d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                                </svg>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
        <% } %>
    </div>

    <script>
        // Clear the "current CV in progress" session attribute when creating a new CV
        function clearSession() {
            // The servlet handles this - just navigate normally
        }
    </script>

</body>
</html>
