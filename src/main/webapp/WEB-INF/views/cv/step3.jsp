<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cvbuilder.model.CV, java.util.List" %>
<%
    CV cv = (CV) request.getAttribute("cv");
    List<String> formations = cv != null ? cv.getFormations() : null;
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Étape 3 – Formation & Loisirs</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
        * { font-family: 'Inter', sans-serif; }
        .form-block { animation: slideIn 0.3s ease; }
        @keyframes slideIn { from { opacity:0; transform: translateY(10px); } to { opacity:1; transform: translateY(0); } }
    </style>
</head>
<body class="min-h-screen bg-slate-50">

    <nav class="bg-white border-b border-slate-200">
        <div class="max-w-3xl mx-auto px-6 py-4 flex items-center justify-between">
            <a href="<%= request.getContextPath() %>/cv/step2" class="flex items-center gap-2 text-slate-500 hover:text-slate-700 text-sm">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
                </svg>
                Retour
            </a>
            <span class="text-slate-400 text-sm">Création de CV</span>
        </div>
    </nav>

    <div class="max-w-3xl mx-auto px-6 py-8">

        <!-- Step Indicator -->
        <div class="flex items-center justify-center gap-0 mb-10">
            <div class="flex items-center">
                <div class="w-9 h-9 rounded-full flex items-center justify-center text-sm font-semibold bg-green-100 text-green-600">✓</div>
                <span class="ml-2 text-sm font-medium text-green-600 hidden sm:block">État civil</span>
            </div>
            <div class="w-16 h-0.5 bg-green-400 mx-3"></div>
            <div class="flex items-center">
                <div class="w-9 h-9 rounded-full flex items-center justify-center text-sm font-semibold bg-green-100 text-green-600">✓</div>
                <span class="ml-2 text-sm font-medium text-green-600 hidden sm:block">Expériences</span>
            </div>
            <div class="w-16 h-0.5 bg-blue-400 mx-3"></div>
            <div class="flex items-center">
                <div class="w-9 h-9 rounded-full flex items-center justify-center text-sm font-semibold bg-blue-600 text-white">3</div>
                <span class="ml-2 text-sm font-medium text-blue-600 hidden sm:block">Formation</span>
            </div>
        </div>

        <!-- Form Card -->
        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-8"
             style="opacity:0; transform: translateY(20px); transition: all 0.4s ease;" id="formCard">

            <form action="<%= request.getContextPath() %>/cv/step3" method="POST">

                <!-- Formations section -->
                <h2 class="text-xl font-bold text-slate-800 mb-1">Formation</h2>
                <p class="text-slate-500 text-sm mb-6">Vos diplômes et études.</p>

                <div id="formationsContainer">
                    <% if (formations != null && !formations.isEmpty()) {
                        for (String form : formations) {
                            String[] parts = form.split("\\|", -1);
                            String diplome = parts.length > 0 ? parts[0] : "";
                            String ecole = parts.length > 1 ? parts[1] : "";
                            String annee = parts.length > 2 ? parts[2] : "";
                            String desc = parts.length > 3 ? parts[3] : "";
                    %>
                    <div class="form-block bg-slate-50 border border-slate-200 rounded-2xl p-5 mb-4 relative">
                        <button type="button" onclick="removeBlock(this)"
                                class="absolute top-4 right-4 text-slate-400 hover:text-red-500 transition-colors">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                            </svg>
                        </button>
                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">Diplôme / Titre</label>
                                <input type="text" name="diplome" value="<%= diplome %>"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                       placeholder="Licence en Informatique">
                            </div>
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">École / Université</label>
                                <input type="text" name="ecole" value="<%= ecole %>"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                       placeholder="Université Hassan II">
                            </div>
                        </div>
                        <div class="mb-4">
                            <label class="block text-xs font-medium text-slate-600 mb-1.5">Année d'obtention</label>
                            <input type="text" name="annee" value="<%= annee %>"
                                   class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                   placeholder="2022">
                        </div>
                        <div>
                            <label class="block text-xs font-medium text-slate-600 mb-1.5">Description <span class="text-slate-400">(optionnel)</span></label>
                            <textarea name="descriptionForm" rows="2"
                                      class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
                                      placeholder="Mention, spécialisation..."><%= desc %></textarea>
                        </div>
                    </div>
                    <% } } else { %>
                    <div class="form-block bg-slate-50 border border-slate-200 rounded-2xl p-5 mb-4 relative">
                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">Diplôme / Titre</label>
                                <input type="text" name="diplome"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                       placeholder="Licence en Informatique">
                            </div>
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">École / Université</label>
                                <input type="text" name="ecole"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                       placeholder="Université Hassan II">
                            </div>
                        </div>
                        <div class="mb-4">
                            <label class="block text-xs font-medium text-slate-600 mb-1.5">Année d'obtention</label>
                            <input type="text" name="annee"
                                   class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                   placeholder="2022">
                        </div>
                        <div>
                            <label class="block text-xs font-medium text-slate-600 mb-1.5">Description <span class="text-slate-400">(optionnel)</span></label>
                            <textarea name="descriptionForm" rows="2"
                                      class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
                                      placeholder="Mention, spécialisation..."></textarea>
                        </div>
                    </div>
                    <% } %>
                </div>

                <button type="button" onclick="addFormation()"
                        class="w-full border-2 border-dashed border-slate-300 hover:border-blue-400 hover:bg-blue-50
                               text-slate-500 hover:text-blue-600 font-medium py-3.5 rounded-xl
                               transition-all duration-200 text-sm flex items-center justify-center gap-2 mb-8">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
                    </svg>
                    Ajouter une formation
                </button>

                <!-- Divider -->
                <hr class="border-slate-100 mb-8">

                <!-- Competences & Langues -->
                <h3 class="text-lg font-bold text-slate-800 mb-5">Compétences & Langues</h3>

                <div class="mb-5">
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Compétences
                        <span class="text-slate-400 font-normal ml-1">(séparées par des virgules)</span>
                    </label>
                    <input type="text" name="competences"
                           value="<%= cv != null && cv.getCompetences() != null ? cv.getCompetences() : "" %>"
                           class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                           placeholder="Java, Spring Boot, React, SQL...">
                </div>

                <div class="mb-8">
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Langues
                        <span class="text-slate-400 font-normal ml-1">(ex: Français B2, Anglais C1)</span>
                    </label>
                    <input type="text" name="langues"
                           value="<%= cv != null && cv.getLangues() != null ? cv.getLangues() : "" %>"
                           class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                           placeholder="Arabe (natif), Français (courant), Anglais (B2)">
                </div>

                <!-- Divider -->
                <hr class="border-slate-100 mb-8">

                <!-- Loisirs -->
                <h3 class="text-lg font-bold text-slate-800 mb-5">Centres d'intérêt</h3>

                <div class="mb-8">
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Loisirs & Hobbies
                        <span class="text-slate-400 font-normal ml-1">(séparés par des virgules)</span>
                    </label>
                    <input type="text" name="loisirs"
                           value="<%= cv != null && cv.getLoisirs() != null ? cv.getLoisirs() : "" %>"
                           class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all text-sm"
                           placeholder="Lecture, Football, Voyages, Photographie...">
                </div>

                <!-- Navigation -->
                <div class="flex justify-between">
                    <a href="<%= request.getContextPath() %>/cv/step2"
                       class="inline-flex items-center gap-2 text-slate-500 hover:text-slate-700 font-medium px-5 py-3 rounded-xl
                              border border-slate-200 hover:bg-slate-50 transition-colors text-sm">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
                        </svg>
                        Précédent
                    </a>
                    <button type="submit"
                            class="inline-flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold
                                   px-6 py-3 rounded-xl transition-colors duration-200 text-sm">
                        Choisir un template
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
                        </svg>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script>
        window.addEventListener('load', () => {
            const card = document.getElementById('formCard');
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        });

        function addFormation() {
            const container = document.getElementById('formationsContainer');
            const div = document.createElement('div');
            div.className = 'form-block bg-slate-50 border border-slate-200 rounded-2xl p-5 mb-4 relative';
            div.innerHTML = `
                <button type="button" onclick="removeBlock(this)"
                        class="absolute top-4 right-4 text-slate-400 hover:text-red-500 transition-colors">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                    </svg>
                </button>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                    <div>
                        <label class="block text-xs font-medium text-slate-600 mb-1.5">Diplôme / Titre</label>
                        <input type="text" name="diplome"
                               class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                               placeholder="Licence en Informatique">
                    </div>
                    <div>
                        <label class="block text-xs font-medium text-slate-600 mb-1.5">École / Université</label>
                        <input type="text" name="ecole"
                               class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                               placeholder="Université Hassan II">
                    </div>
                </div>
                <div class="mb-4">
                    <label class="block text-xs font-medium text-slate-600 mb-1.5">Année d'obtention</label>
                    <input type="text" name="annee"
                           class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                           placeholder="2022">
                </div>
                <div>
                    <label class="block text-xs font-medium text-slate-600 mb-1.5">Description <span class="text-slate-400">(optionnel)</span></label>
                    <textarea name="descriptionForm" rows="2"
                              class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
                              placeholder="Mention, spécialisation..."></textarea>
                </div>
            `;
            container.appendChild(div);
        }

        function removeBlock(btn) {
            const block = btn.closest('.form-block');
            block.style.opacity = '0';
            block.style.transform = 'translateX(-10px)';
            block.style.transition = 'all 0.2s ease';
            setTimeout(() => block.remove(), 200);
        }
    </script>
</body>
</html>
