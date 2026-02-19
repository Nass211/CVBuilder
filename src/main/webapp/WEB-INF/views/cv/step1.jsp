<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cvbuilder.model.CV" %>
<%
    CV cv = (CV) request.getAttribute("cv");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Étape 1 – État Civil</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
        * { font-family: 'Inter', sans-serif; }
        .input-field {
            @apply w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800
                   placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500
                   focus:border-transparent transition-all text-sm;
        }
        .step-active { background: #2563EB; color: white; }
        .step-done { background: #DCFCE7; color: #15803D; }
        .step-todo { background: #F1F5F9; color: #94A3B8; }
    </style>
</head>
<body class="min-h-screen bg-slate-50">

    <!-- Top Nav -->
    <nav class="bg-white border-b border-slate-200">
        <div class="max-w-3xl mx-auto px-6 py-4 flex items-center justify-between">
            <a href="<%= request.getContextPath() %>/dashboard" class="flex items-center gap-2 text-slate-500 hover:text-slate-700 text-sm">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
                </svg>
                Dashboard
            </a>
            <span class="text-slate-400 text-sm">Création de CV</span>
        </div>
    </nav>

    <div class="max-w-3xl mx-auto px-6 py-8">

        <!-- Step Indicator -->
        <div class="flex items-center justify-center gap-0 mb-10" id="stepIndicator">
            <!-- Step 1 -->
            <div class="flex items-center">
                <div class="step-active w-9 h-9 rounded-full flex items-center justify-center text-sm font-semibold transition-all duration-300">
                    1
                </div>
                <span class="ml-2 text-sm font-medium text-blue-600 hidden sm:block">État civil</span>
            </div>
            <!-- Line -->
            <div class="w-16 h-0.5 bg-slate-200 mx-3"></div>
            <!-- Step 2 -->
            <div class="flex items-center">
                <div class="step-todo w-9 h-9 rounded-full flex items-center justify-center text-sm font-semibold">
                    2
                </div>
                <span class="ml-2 text-sm font-medium text-slate-400 hidden sm:block">Expériences</span>
            </div>
            <!-- Line -->
            <div class="w-16 h-0.5 bg-slate-200 mx-3"></div>
            <!-- Step 3 -->
            <div class="flex items-center">
                <div class="step-todo w-9 h-9 rounded-full flex items-center justify-center text-sm font-semibold">
                    3
                </div>
                <span class="ml-2 text-sm font-medium text-slate-400 hidden sm:block">Formation</span>
            </div>
        </div>

        <!-- Form Card -->
        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-8" id="formCard"
             style="opacity:0; transform: translateY(20px); transition: all 0.4s ease;">

            <h2 class="text-xl font-bold text-slate-800 mb-1">État Civil</h2>
            <p class="text-slate-500 text-sm mb-7">Renseignez vos informations personnelles.</p>

            <form action="<%= request.getContextPath() %>/cv/step1" method="POST">

                <!-- CV Title -->
                <div class="mb-6 pb-6 border-b border-slate-100">
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Titre du CV <span class="text-red-400">*</span>
                        <span class="text-slate-400 font-normal ml-1">(ex: CV Développeur Web)</span>
                    </label>
                    <input type="text" name="title" required
                           value="<%= cv != null && cv.getTitle() != null ? cv.getTitle() : "" %>"
                           class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                           placeholder="Donnez un titre à ce CV">
                </div>

                <!-- Name row -->
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Prénom <span class="text-red-400">*</span></label>
                        <input type="text" name="prenom" required
                               value="<%= cv != null && cv.getPrenom() != null ? cv.getPrenom() : "" %>"
                               class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                               placeholder="Votre prénom">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Nom <span class="text-red-400">*</span></label>
                        <input type="text" name="nom" required
                               value="<%= cv != null && cv.getNom() != null ? cv.getNom() : "" %>"
                               class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                               placeholder="Votre nom">
                    </div>
                </div>

                <!-- Contact row -->
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Email <span class="text-red-400">*</span></label>
                        <input type="email" name="email" required
                               value="<%= cv != null && cv.getEmail() != null ? cv.getEmail() : "" %>"
                               class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                               placeholder="email@exemple.com">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Téléphone</label>
                        <input type="tel" name="telephone"
                               value="<%= cv != null && cv.getTelephone() != null ? cv.getTelephone() : "" %>"
                               class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                               placeholder="+212 6 00 00 00 00">
                    </div>
                </div>

                <!-- Address -->
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Adresse</label>
                        <input type="text" name="adresse"
                               value="<%= cv != null && cv.getAdresse() != null ? cv.getAdresse() : "" %>"
                               class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                               placeholder="Rue, numéro...">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Ville</label>
                        <input type="text" name="ville"
                               value="<%= cv != null && cv.getVille() != null ? cv.getVille() : "" %>"
                               class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                               placeholder="Casablanca">
                    </div>
                </div>

                <!-- More info -->
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Date de naissance</label>
                        <input type="date" name="dateNaissance"
                               value="<%= cv != null && cv.getDateNaissance() != null ? cv.getDateNaissance() : "" %>"
                               class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Nationalité</label>
                        <input type="text" name="nationalite"
                               value="<%= cv != null && cv.getNationalite() != null ? cv.getNationalite() : "" %>"
                               class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                               placeholder="Marocaine">
                    </div>
                </div>

                <!-- Online presence -->
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">LinkedIn</label>
                        <input type="url" name="linkedin"
                               value="<%= cv != null && cv.getLinkedin() != null ? cv.getLinkedin() : "" %>"
                               class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                               placeholder="https://linkedin.com/in/...">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-slate-700 mb-2">Site web / Portfolio</label>
                        <input type="url" name="siteWeb"
                               value="<%= cv != null && cv.getSiteWeb() != null ? cv.getSiteWeb() : "" %>"
                               class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                               placeholder="https://monsite.com">
                    </div>
                </div>

                <!-- Summary -->
                <div class="mb-7">
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Résumé professionnel
                        <span class="text-slate-400 font-normal ml-1">(2-3 phrases sur vous)</span>
                    </label>
                    <textarea name="resume" rows="3"
                              class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm resize-none"
                              placeholder="Développeur passionné avec 3 ans d'expérience..."><%= cv != null && cv.getResume() != null ? cv.getResume() : "" %></textarea>
                </div>

                <!-- Submit -->
                <div class="flex justify-end">
                    <button type="submit"
                            class="inline-flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold
                                   px-6 py-3 rounded-xl transition-colors duration-200">
                        Suivant : Expériences
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
                        </svg>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Animate the form card in when page loads
        window.addEventListener('load', () => {
            const card = document.getElementById('formCard');
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        });
    </script>
</body>
</html>
