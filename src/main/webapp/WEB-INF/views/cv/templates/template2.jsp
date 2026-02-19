<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cvbuilder.model.CV, java.util.List" %>
<%
    CV cv = (CV) request.getAttribute("cv");
    List<String> experiences = cv.getExperiences();
    List<String> formations = cv.getFormations();
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CV – <%= cv.getFullName() %></title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
        * { font-family: 'Inter', sans-serif; }
        @media print {
            .no-print { display: none !important; }
            body { background: white; }
            .cv-paper { box-shadow: none !important; margin: 0 !important; }
        }
    </style>
</head>
<body class="bg-slate-100 min-h-screen">

    <!-- Action bar -->
    <div class="no-print bg-white border-b border-slate-200 sticky top-0 z-10">
        <div class="max-w-4xl mx-auto px-6 py-3 flex items-center justify-between">
            <a href="<%= request.getContextPath() %>/dashboard" class="flex items-center gap-2 text-slate-500 hover:text-slate-700 text-sm">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
                </svg>
                Dashboard
            </a>
            <span class="font-semibold text-slate-700 text-sm">Template : Moderne Teal</span>
            <div class="flex items-center gap-3">
                <a href="<%= request.getContextPath() %>/cv/step1?id=<%= cv.getId() %>"
                   class="text-sm text-slate-500 hover:text-slate-700 border border-slate-200 px-4 py-2 rounded-xl hover:bg-slate-50 transition-colors">
                    ✏️ Modifier
                </a>
                <a href="<%= request.getContextPath() %>/cv/template"
                   class="text-sm text-slate-500 hover:text-slate-700 border border-slate-200 px-4 py-2 rounded-xl hover:bg-slate-50 transition-colors">
                    🎨 Changer template
                </a>
                <a href="<%= request.getContextPath() %>/cv/download?id=<%= cv.getId() %>"
                   class="inline-flex items-center gap-2 bg-teal-600 hover:bg-teal-700 text-white text-sm font-medium px-4 py-2 rounded-xl transition-colors">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"/>
                    </svg>
                    Télécharger PDF
                </a>
            </div>
        </div>
    </div>

    <div class="max-w-3xl mx-auto my-8 px-4">
    <div class="cv-paper bg-white shadow-xl rounded-lg overflow-hidden flex">

        <!-- SIDEBAR -->
        <div class="w-64 bg-teal-800 text-white flex-shrink-0 flex flex-col">
            <!-- Avatar area -->
            <div class="bg-teal-900 px-6 py-8 text-center">
                <div class="w-20 h-20 bg-teal-600 rounded-full mx-auto mb-4 flex items-center justify-center text-3xl font-bold">
                    <%= cv.getPrenom() != null && !cv.getPrenom().isEmpty() ? cv.getPrenom().charAt(0) : "?" %>
                </div>
                <h1 class="text-lg font-bold leading-tight"><%= cv.getFullName() %></h1>
                <% if (cv.getTitle() != null && !cv.getTitle().isEmpty()) { %>
                <p class="text-teal-300 text-xs mt-1.5"><%= cv.getTitle() %></p>
                <% } %>
            </div>

            <div class="px-5 py-6 space-y-5 flex-1">
                <!-- Contact -->
                <div>
                    <h3 class="text-teal-300 text-xs font-bold uppercase tracking-wider mb-3">Contact</h3>
                    <div class="space-y-2 text-xs text-teal-100">
                        <% if (cv.getEmail() != null && !cv.getEmail().isEmpty()) { %>
                        <p class="break-all">✉ <%= cv.getEmail() %></p>
                        <% } %>
                        <% if (cv.getTelephone() != null && !cv.getTelephone().isEmpty()) { %>
                        <p>📞 <%= cv.getTelephone() %></p>
                        <% } %>
                        <% if (cv.getVille() != null && !cv.getVille().isEmpty()) { %>
                        <p>📍 <%= cv.getVille() %></p>
                        <% } %>
                        <% if (cv.getLinkedin() != null && !cv.getLinkedin().isEmpty()) { %>
                        <p>🔗 LinkedIn</p>
                        <% } %>
                    </div>
                </div>

                <!-- Skills -->
                <% if (cv.getCompetences() != null && !cv.getCompetences().trim().isEmpty()) { %>
                <div>
                    <h3 class="text-teal-300 text-xs font-bold uppercase tracking-wider mb-3">Compétences</h3>
                    <div class="space-y-1.5">
                        <% for (String skill : cv.getCompetences().split(",")) {
                            skill = skill.trim();
                            if (!skill.isEmpty()) { %>
                        <div class="bg-teal-700 text-teal-100 text-xs px-2.5 py-1 rounded-full inline-block mr-1 mb-1"><%= skill %></div>
                        <% } } %>
                    </div>
                </div>
                <% } %>

                <!-- Languages -->
                <% if (cv.getLangues() != null && !cv.getLangues().trim().isEmpty()) { %>
                <div>
                    <h3 class="text-teal-300 text-xs font-bold uppercase tracking-wider mb-3">Langues</h3>
                    <div class="space-y-1.5 text-xs text-teal-100">
                        <% for (String lang : cv.getLangues().split(",")) {
                            lang = lang.trim();
                            if (!lang.isEmpty()) { %>
                        <p>• <%= lang %></p>
                        <% } } %>
                    </div>
                </div>
                <% } %>

                <!-- Loisirs -->
                <% if (cv.getLoisirs() != null && !cv.getLoisirs().trim().isEmpty()) { %>
                <div>
                    <h3 class="text-teal-300 text-xs font-bold uppercase tracking-wider mb-3">Intérêts</h3>
                    <p class="text-xs text-teal-100 leading-relaxed"><%= cv.getLoisirs() %></p>
                </div>
                <% } %>
            </div>
        </div>

        <!-- MAIN CONTENT -->
        <div class="flex-1 px-8 py-8 space-y-7">

            <!-- Résumé -->
            <% if (cv.getResume() != null && !cv.getResume().trim().isEmpty()) { %>
            <section>
                <h2 class="text-xs font-bold text-teal-700 uppercase tracking-widest mb-3 pb-1.5 border-b-2 border-teal-200">Profil</h2>
                <p class="text-slate-600 text-sm leading-relaxed"><%= cv.getResume() %></p>
            </section>
            <% } %>

            <!-- Expériences -->
            <% if (experiences != null && !experiences.isEmpty()) { %>
            <section>
                <h2 class="text-xs font-bold text-teal-700 uppercase tracking-widest mb-4 pb-1.5 border-b-2 border-teal-200">
                    Expériences
                </h2>
                <div class="space-y-5">
                    <% for (String exp : experiences) {
                        String[] p = exp.split("\\|", -1);
                        String poste = p.length > 0 ? p[0] : "";
                        String entreprise = p.length > 1 ? p[1] : "";
                        String dateDebut = p.length > 2 ? p[2] : "";
                        String dateFin = p.length > 3 ? p[3] : "";
                        String desc = p.length > 4 ? p[4] : "";
                    %>
                    <div class="border-l-2 border-teal-200 pl-4">
                        <h3 class="font-semibold text-slate-800 text-sm"><%= poste %></h3>
                        <div class="flex items-center gap-2 mt-0.5">
                            <span class="text-teal-600 text-xs font-medium"><%= entreprise %></span>
                            <span class="text-slate-300 text-xs">|</span>
                            <span class="text-slate-400 text-xs">
                                <%= dateDebut %><% if (!dateFin.isEmpty()) { %> – <%= dateFin %><% } else { %> – Présent<% } %>
                            </span>
                        </div>
                        <% if (!desc.isEmpty()) { %>
                        <p class="text-slate-500 text-xs mt-2 leading-relaxed"><%= desc %></p>
                        <% } %>
                    </div>
                    <% } %>
                </div>
            </section>
            <% } %>

            <!-- Formation -->
            <% if (formations != null && !formations.isEmpty()) { %>
            <section>
                <h2 class="text-xs font-bold text-teal-700 uppercase tracking-widest mb-4 pb-1.5 border-b-2 border-teal-200">Formation</h2>
                <div class="space-y-4">
                    <% for (String form : formations) {
                        String[] p = form.split("\\|", -1);
                        String diplome = p.length > 0 ? p[0] : "";
                        String ecole = p.length > 1 ? p[1] : "";
                        String annee = p.length > 2 ? p[2] : "";
                        String desc = p.length > 3 ? p[3] : "";
                    %>
                    <div class="border-l-2 border-teal-200 pl-4">
                        <h3 class="font-semibold text-slate-800 text-sm"><%= diplome %></h3>
                        <div class="flex items-center gap-2">
                            <span class="text-teal-600 text-xs"><%= ecole %></span>
                            <% if (!annee.isEmpty()) { %><span class="text-slate-300 text-xs">|</span><span class="text-slate-400 text-xs"><%= annee %></span><% } %>
                        </div>
                        <% if (!desc.isEmpty()) { %><p class="text-slate-400 text-xs mt-1"><%= desc %></p><% } %>
                    </div>
                    <% } %>
                </div>
            </section>
            <% } %>
        </div>
    </div>
    </div>
</body>
</html>
