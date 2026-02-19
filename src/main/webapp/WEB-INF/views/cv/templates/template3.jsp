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
            <span class="font-semibold text-slate-700 text-sm">Template : Créatif Violet</span>
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
                   class="inline-flex items-center gap-2 bg-violet-600 hover:bg-violet-700 text-white text-sm font-medium px-4 py-2 rounded-xl transition-colors">
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
    <div class="cv-paper bg-white shadow-xl rounded-lg overflow-hidden">

        <!-- HEADER with left accent bar -->
        <div class="border-l-8 border-violet-500 px-8 py-8 bg-white">
            <div class="flex items-start justify-between">
                <div>
                    <h1 class="text-4xl font-bold text-slate-900 tracking-tight"><%= cv.getFullName() %></h1>
                    <% if (cv.getTitle() != null && !cv.getTitle().isEmpty()) { %>
                    <p class="text-violet-600 text-lg font-medium mt-1"><%= cv.getTitle() %></p>
                    <% } %>
                </div>
                <!-- Initials bubble -->
                <div class="w-16 h-16 bg-violet-600 rounded-2xl flex items-center justify-center text-white text-2xl font-bold flex-shrink-0 ml-6">
                    <%= cv.getPrenom() != null && !cv.getPrenom().isEmpty() ? cv.getPrenom().charAt(0) : "?" %>
                </div>
            </div>

            <!-- Contact tags -->
            <div class="flex flex-wrap gap-2 mt-4">
                <% if (cv.getEmail() != null && !cv.getEmail().isEmpty()) { %>
                <span class="bg-violet-50 text-violet-700 text-xs px-3 py-1.5 rounded-full font-medium">✉ <%= cv.getEmail() %></span>
                <% } %>
                <% if (cv.getTelephone() != null && !cv.getTelephone().isEmpty()) { %>
                <span class="bg-violet-50 text-violet-700 text-xs px-3 py-1.5 rounded-full font-medium">📞 <%= cv.getTelephone() %></span>
                <% } %>
                <% if (cv.getVille() != null && !cv.getVille().isEmpty()) { %>
                <span class="bg-violet-50 text-violet-700 text-xs px-3 py-1.5 rounded-full font-medium">📍 <%= cv.getVille() %></span>
                <% } %>
                <% if (cv.getLinkedin() != null && !cv.getLinkedin().isEmpty()) { %>
                <span class="bg-violet-50 text-violet-700 text-xs px-3 py-1.5 rounded-full font-medium">🔗 LinkedIn</span>
                <% } %>
            </div>
        </div>

        <div class="px-8 pb-8 space-y-7">

            <!-- RÉSUMÉ -->
            <% if (cv.getResume() != null && !cv.getResume().trim().isEmpty()) { %>
            <section class="bg-violet-50 rounded-2xl p-5">
                <p class="text-slate-600 text-sm leading-relaxed italic">"<%= cv.getResume() %>"</p>
            </section>
            <% } %>

            <!-- EXPÉRIENCES -->
            <% if (experiences != null && !experiences.isEmpty()) { %>
            <section>
                <div class="flex items-center gap-3 mb-5">
                    <div class="w-1 h-5 bg-violet-500 rounded-full"></div>
                    <h2 class="text-sm font-bold text-slate-700 uppercase tracking-widest">Expériences Professionnelles</h2>
                </div>
                <div class="space-y-5">
                    <% for (String exp : experiences) {
                        String[] p = exp.split("\\|", -1);
                        String poste = p.length > 0 ? p[0] : "";
                        String entreprise = p.length > 1 ? p[1] : "";
                        String dateDebut = p.length > 2 ? p[2] : "";
                        String dateFin = p.length > 3 ? p[3] : "";
                        String desc = p.length > 4 ? p[4] : "";
                    %>
                    <div class="flex gap-4">
                        <div class="flex flex-col items-center">
                            <div class="w-3 h-3 bg-violet-500 rounded-full mt-1 flex-shrink-0"></div>
                            <div class="w-0.5 bg-violet-100 flex-1 mt-1"></div>
                        </div>
                        <div class="pb-4 flex-1">
                            <div class="flex items-start justify-between">
                                <h3 class="font-semibold text-slate-800"><%= poste %></h3>
                                <span class="text-xs text-violet-500 font-medium bg-violet-50 px-2.5 py-1 rounded-full ml-4 whitespace-nowrap">
                                    <%= dateDebut %><% if (!dateFin.isEmpty()) { %> – <%= dateFin %><% } else { %> – Présent<% } %>
                                </span>
                            </div>
                            <p class="text-violet-600 text-sm mt-0.5"><%= entreprise %></p>
                            <% if (!desc.isEmpty()) { %>
                            <p class="text-slate-500 text-sm mt-2 leading-relaxed"><%= desc %></p>
                            <% } %>
                        </div>
                    </div>
                    <% } %>
                </div>
            </section>
            <% } %>

            <!-- FORMATION -->
            <% if (formations != null && !formations.isEmpty()) { %>
            <section>
                <div class="flex items-center gap-3 mb-5">
                    <div class="w-1 h-5 bg-violet-500 rounded-full"></div>
                    <h2 class="text-sm font-bold text-slate-700 uppercase tracking-widest">Formation</h2>
                </div>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    <% for (String form : formations) {
                        String[] p = form.split("\\|", -1);
                        String diplome = p.length > 0 ? p[0] : "";
                        String ecole = p.length > 1 ? p[1] : "";
                        String annee = p.length > 2 ? p[2] : "";
                        String desc = p.length > 3 ? p[3] : "";
                    %>
                    <div class="bg-slate-50 rounded-xl p-4">
                        <h3 class="font-semibold text-slate-800 text-sm"><%= diplome %></h3>
                        <p class="text-violet-600 text-xs mt-0.5"><%= ecole %></p>
                        <% if (!annee.isEmpty()) { %><p class="text-slate-400 text-xs"><%= annee %></p><% } %>
                        <% if (!desc.isEmpty()) { %><p class="text-slate-500 text-xs mt-1"><%= desc %></p><% } %>
                    </div>
                    <% } %>
                </div>
            </section>
            <% } %>

            <!-- COMPÉTENCES & LANGUES & LOISIRS (bottom row) -->
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-5">
                <% if (cv.getCompetences() != null && !cv.getCompetences().trim().isEmpty()) { %>
                <section>
                    <div class="flex items-center gap-2 mb-3">
                        <div class="w-1 h-4 bg-violet-500 rounded-full"></div>
                        <h2 class="text-xs font-bold text-slate-600 uppercase tracking-widest">Compétences</h2>
                    </div>
                    <div class="flex flex-wrap gap-1.5">
                        <% for (String skill : cv.getCompetences().split(",")) {
                            skill = skill.trim();
                            if (!skill.isEmpty()) { %>
                        <span class="bg-violet-100 text-violet-700 text-xs px-2 py-1 rounded-lg"><%= skill %></span>
                        <% } } %>
                    </div>
                </section>
                <% } %>

                <% if (cv.getLangues() != null && !cv.getLangues().trim().isEmpty()) { %>
                <section>
                    <div class="flex items-center gap-2 mb-3">
                        <div class="w-1 h-4 bg-violet-500 rounded-full"></div>
                        <h2 class="text-xs font-bold text-slate-600 uppercase tracking-widest">Langues</h2>
                    </div>
                    <div class="space-y-1">
                        <% for (String lang : cv.getLangues().split(",")) {
                            lang = lang.trim();
                            if (!lang.isEmpty()) { %>
                        <p class="text-slate-600 text-xs">• <%= lang %></p>
                        <% } } %>
                    </div>
                </section>
                <% } %>

                <% if (cv.getLoisirs() != null && !cv.getLoisirs().trim().isEmpty()) { %>
                <section>
                    <div class="flex items-center gap-2 mb-3">
                        <div class="w-1 h-4 bg-violet-500 rounded-full"></div>
                        <h2 class="text-xs font-bold text-slate-600 uppercase tracking-widest">Intérêts</h2>
                    </div>
                    <p class="text-slate-500 text-xs leading-relaxed"><%= cv.getLoisirs() %></p>
                </section>
                <% } %>
            </div>

        </div>
    </div>
    </div>
</body>
</html>
