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

    <!-- Action bar - NOT printed -->
    <div class="no-print bg-white border-b border-slate-200 sticky top-0 z-10 print:hidden">
        <div class="max-w-4xl mx-auto px-6 py-3 flex items-center justify-between">
            <a href="<%= request.getContextPath() %>/dashboard"
               class="flex items-center gap-2 text-slate-500 hover:text-slate-700 text-sm">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
                </svg>
                Dashboard
            </a>
            <span class="font-semibold text-slate-700 text-sm">Template : Classique Bleu</span>
            <div class="flex items-center gap-3">
                <a href="<%= request.getContextPath() %>/cv/step1?id=<%= cv.getId() %>"
                   class="text-sm text-slate-500 hover:text-slate-700 border border-slate-200 px-4 py-2 rounded-xl hover:bg-slate-50 transition-colors">
                    ✏️ Modifier
                </a>
                <a href="<%= request.getContextPath() %>/cv/template?id=<%= cv.getId() %>"
                   class="text-sm text-slate-500 hover:text-slate-700 border border-slate-200 px-4 py-2 rounded-xl hover:bg-slate-50 transition-colors">
                    🎨 Changer template
                </a>
                <a href="<%= request.getContextPath() %>/cv/download?id=<%= cv.getId() %>"
                   class="inline-flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium px-4 py-2 rounded-xl transition-colors">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"/>
                    </svg>
                    Télécharger PDF
                </a>
            </div>
        </div>
    </div>

    <!-- CV Paper -->
    <div class="max-w-3xl mx-auto my-8 px-4">
    <div class="cv-paper bg-white shadow-xl rounded-lg overflow-hidden">

        <!-- HEADER -->
        <div class="bg-blue-600 text-white px-10 py-8">
            <h1 class="text-3xl font-bold tracking-wide"><%= cv.getFullName().toUpperCase() %></h1>
            <% if (cv.getTitle() != null && !cv.getTitle().isEmpty()) { %>
            <p class="text-blue-200 text-base mt-1 font-medium"><%= cv.getTitle() %></p>
            <% } %>

            <!-- Contact chips -->
            <div class="flex flex-wrap gap-x-5 gap-y-1 mt-4 text-sm text-blue-100">
                <% if (cv.getEmail() != null && !cv.getEmail().isEmpty()) { %>
                <span>✉ <%= cv.getEmail() %></span>
                <% } %>
                <% if (cv.getTelephone() != null && !cv.getTelephone().isEmpty()) { %>
                <span>📞 <%= cv.getTelephone() %></span>
                <% } %>
                <% if (cv.getVille() != null && !cv.getVille().isEmpty()) { %>
                <span>📍 <%= cv.getVille() %></span>
                <% } %>
                <% if (cv.getLinkedin() != null && !cv.getLinkedin().isEmpty()) { %>
                <span>🔗 LinkedIn</span>
                <% } %>
            </div>
        </div>

        <div class="px-10 py-8 space-y-7">

            <!-- RÉSUMÉ -->
            <% if (cv.getResume() != null && !cv.getResume().trim().isEmpty()) { %>
            <section>
                <h2 class="text-xs font-bold text-blue-600 uppercase tracking-widest mb-3 pb-1 border-b-2 border-blue-600">À Propos</h2>
                <p class="text-slate-600 text-sm leading-relaxed"><%= cv.getResume() %></p>
            </section>
            <% } %>

            <!-- EXPÉRIENCES -->
            <% if (experiences != null && !experiences.isEmpty()) { %>
            <section>
                <h2 class="text-xs font-bold text-blue-600 uppercase tracking-widest mb-4 pb-1 border-b-2 border-blue-600">
                    Expériences Professionnelles
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
                    <div>
                        <div class="flex items-start justify-between">
                            <div>
                                <h3 class="font-semibold text-slate-800"><%= poste %></h3>
                                <p class="text-blue-600 text-sm font-medium"><%= entreprise %></p>
                            </div>
                            <span class="text-xs text-slate-400 bg-slate-50 px-2.5 py-1 rounded-full whitespace-nowrap ml-4">
                                <%= dateDebut %><% if (!dateFin.isEmpty()) { %> – <%= dateFin %><% } else { %> – Présent<% } %>
                            </span>
                        </div>
                        <% if (!desc.isEmpty()) { %>
                        <p class="text-slate-600 text-sm mt-2 leading-relaxed"><%= desc %></p>
                        <% } %>
                    </div>
                    <% } %>
                </div>
            </section>
            <% } %>

            <!-- FORMATION -->
            <% if (formations != null && !formations.isEmpty()) { %>
            <section>
                <h2 class="text-xs font-bold text-blue-600 uppercase tracking-widest mb-4 pb-1 border-b-2 border-blue-600">Formation</h2>
                <div class="space-y-4">
                    <% for (String form : formations) {
                        String[] p = form.split("\\|", -1);
                        String diplome = p.length > 0 ? p[0] : "";
                        String ecole = p.length > 1 ? p[1] : "";
                        String annee = p.length > 2 ? p[2] : "";
                        String desc = p.length > 3 ? p[3] : "";
                    %>
                    <div class="flex items-start justify-between">
                        <div>
                            <h3 class="font-semibold text-slate-800"><%= diplome %></h3>
                            <p class="text-slate-500 text-sm"><%= ecole %></p>
                            <% if (!desc.isEmpty()) { %><p class="text-slate-400 text-xs mt-1"><%= desc %></p><% } %>
                        </div>
                        <% if (!annee.isEmpty()) { %>
                        <span class="text-xs text-slate-400 bg-slate-50 px-2.5 py-1 rounded-full ml-4"><%= annee %></span>
                        <% } %>
                    </div>
                    <% } %>
                </div>
            </section>
            <% } %>

            <!-- COMPÉTENCES & LANGUES -->
            <% boolean hasSkills = (cv.getCompetences() != null && !cv.getCompetences().trim().isEmpty())
                                || (cv.getLangues() != null && !cv.getLangues().trim().isEmpty()); %>
            <% if (hasSkills) { %>
            <section>
                <h2 class="text-xs font-bold text-blue-600 uppercase tracking-widest mb-4 pb-1 border-b-2 border-blue-600">Compétences & Langues</h2>
                <% if (cv.getCompetences() != null && !cv.getCompetences().trim().isEmpty()) { %>
                <div class="mb-3">
                    <p class="text-xs font-semibold text-slate-500 uppercase mb-2">Compétences</p>
                    <div class="flex flex-wrap gap-2">
                        <% for (String skill : cv.getCompetences().split(",")) {
                            skill = skill.trim();
                            if (!skill.isEmpty()) { %>
                        <span class="bg-blue-50 text-blue-700 text-xs font-medium px-3 py-1.5 rounded-full"><%= skill %></span>
                        <% } } %>
                    </div>
                </div>
                <% } %>
                <% if (cv.getLangues() != null && !cv.getLangues().trim().isEmpty()) { %>
                <div>
                    <p class="text-xs font-semibold text-slate-500 uppercase mb-2">Langues</p>
                    <div class="flex flex-wrap gap-2">
                        <% for (String lang : cv.getLangues().split(",")) {
                            lang = lang.trim();
                            if (!lang.isEmpty()) { %>
                        <span class="bg-slate-100 text-slate-700 text-xs font-medium px-3 py-1.5 rounded-full"><%= lang %></span>
                        <% } } %>
                    </div>
                </div>
                <% } %>
            </section>
            <% } %>

            <!-- LOISIRS -->
            <% if (cv.getLoisirs() != null && !cv.getLoisirs().trim().isEmpty()) { %>
            <section>
                <h2 class="text-xs font-bold text-blue-600 uppercase tracking-widest mb-3 pb-1 border-b-2 border-blue-600">Centres d'Intérêt</h2>
                <p class="text-slate-600 text-sm"><%= cv.getLoisirs() %></p>
            </section>
            <% } %>

        </div>
    </div>
    </div>
</body>
</html>
