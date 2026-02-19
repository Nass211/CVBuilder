<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cvbuilder.model.CV, java.util.List" %>
<%
    CV cv = (CV) request.getAttribute("cv");
    List<String> experiences = cv != null ? cv.getExperiences() : null;
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Étape 2 – Expériences</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
        * { font-family: 'Inter', sans-serif; }
        .exp-block { animation: slideIn 0.3s ease; }
        @keyframes slideIn { from { opacity:0; transform: translateY(10px); } to { opacity:1; transform: translateY(0); } }
    </style>
</head>
<body class="min-h-screen bg-slate-50">

    <nav class="bg-white border-b border-slate-200">
        <div class="max-w-3xl mx-auto px-6 py-4 flex items-center justify-between">
            <a href="<%= request.getContextPath() %>/cv/step1" class="flex items-center gap-2 text-slate-500 hover:text-slate-700 text-sm">
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
                <div class="w-9 h-9 rounded-full flex items-center justify-center text-sm font-semibold bg-green-100 text-green-600">
                    ✓
                </div>
                <span class="ml-2 text-sm font-medium text-green-600 hidden sm:block">État civil</span>
            </div>
            <div class="w-16 h-0.5 bg-blue-400 mx-3"></div>
            <div class="flex items-center">
                <div class="w-9 h-9 rounded-full flex items-center justify-center text-sm font-semibold bg-blue-600 text-white">
                    2
                </div>
                <span class="ml-2 text-sm font-medium text-blue-600 hidden sm:block">Expériences</span>
            </div>
            <div class="w-16 h-0.5 bg-slate-200 mx-3"></div>
            <div class="flex items-center">
                <div class="w-9 h-9 rounded-full flex items-center justify-center text-sm font-semibold bg-slate-100 text-slate-400">
                    3
                </div>
                <span class="ml-2 text-sm font-medium text-slate-400 hidden sm:block">Formation</span>
            </div>
        </div>

        <!-- Form Card -->
        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-8"
             style="opacity:0; transform: translateY(20px); transition: all 0.4s ease;" id="formCard">

            <h2 class="text-xl font-bold text-slate-800 mb-1">Expériences Professionnelles</h2>
            <p class="text-slate-500 text-sm mb-7">Ajoutez vos expériences. Vous pouvez en ajouter plusieurs.</p>

            <form action="<%= request.getContextPath() %>/cv/step2" method="POST">

                <!-- Experiences container -->
                <div id="experiencesContainer">
                    <% if (experiences != null && !experiences.isEmpty()) {
                        for (String exp : experiences) {
                            String[] parts = exp.split("\\|", -1);
                            String poste = parts.length > 0 ? parts[0] : "";
                            String entreprise = parts.length > 1 ? parts[1] : "";
                            String dateDebut = parts.length > 2 ? parts[2] : "";
                            String dateFin = parts.length > 3 ? parts[3] : "";
                            String desc = parts.length > 4 ? parts[4] : "";
                    %>
                    <div class="exp-block bg-slate-50 border border-slate-200 rounded-2xl p-5 mb-4 relative">
                        <button type="button" onclick="removeExperience(this)"
                                class="absolute top-4 right-4 text-slate-400 hover:text-red-500 transition-colors">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                            </svg>
                        </button>
                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">Poste / Titre</label>
                                <input type="text" name="poste" value="<%= poste %>"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                       placeholder="Développeur Frontend">
                            </div>
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">Entreprise</label>
                                <input type="text" name="entreprise" value="<%= entreprise %>"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                       placeholder="Nom de l'entreprise">
                            </div>
                        </div>
                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">Date de début</label>
                                <input type="month" name="dateDebut" value="<%= dateDebut %>"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                            </div>
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">Date de fin <span class="text-slate-400">(vide = en cours)</span></label>
                                <input type="month" name="dateFin" value="<%= dateFin %>"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                            </div>
                        </div>
                        <div>
                            <label class="block text-xs font-medium text-slate-600 mb-1.5">Description des missions</label>
                            <textarea name="descriptionExp" rows="2"
                                      class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
                                      placeholder="Décrivez vos responsabilités..."><%= desc %></textarea>
                        </div>
                    </div>
                    <% } } else { %>
                    <!-- Default first empty block -->
                    <div class="exp-block bg-slate-50 border border-slate-200 rounded-2xl p-5 mb-4 relative">
                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">Poste / Titre</label>
                                <input type="text" name="poste"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                       placeholder="Développeur Frontend">
                            </div>
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">Entreprise</label>
                                <input type="text" name="entreprise"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                       placeholder="Nom de l'entreprise">
                            </div>
                        </div>
                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">Date de début</label>
                                <input type="month" name="dateDebut"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                            </div>
                            <div>
                                <label class="block text-xs font-medium text-slate-600 mb-1.5">Date de fin <span class="text-slate-400">(vide = en cours)</span></label>
                                <input type="month" name="dateFin"
                                       class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                            </div>
                        </div>
                        <div>
                            <label class="block text-xs font-medium text-slate-600 mb-1.5">Description des missions</label>
                            <textarea name="descriptionExp" rows="2"
                                      class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
                                      placeholder="Décrivez vos responsabilités..."></textarea>
                        </div>
                    </div>
                    <% } %>
                </div>

                <!-- Add experience button -->
                <button type="button" onclick="addExperience()"
                        class="w-full border-2 border-dashed border-slate-300 hover:border-blue-400 hover:bg-blue-50
                               text-slate-500 hover:text-blue-600 font-medium py-3.5 rounded-xl
                               transition-all duration-200 text-sm flex items-center justify-center gap-2 mb-7">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
                    </svg>
                    Ajouter une expérience
                </button>

                <!-- Navigation -->
                <div class="flex justify-between">
                    <a href="<%= request.getContextPath() %>/cv/step1"
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
                        Suivant : Formation
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
                        </svg>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Animate in
        window.addEventListener('load', () => {
            const card = document.getElementById('formCard');
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        });

        // Template for a new experience block
        function addExperience() {
            const container = document.getElementById('experiencesContainer');
            const div = document.createElement('div');
            div.className = 'exp-block bg-slate-50 border border-slate-200 rounded-2xl p-5 mb-4 relative';
            div.innerHTML = `
                <button type="button" onclick="removeExperience(this)"
                        class="absolute top-4 right-4 text-slate-400 hover:text-red-500 transition-colors">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                    </svg>
                </button>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                    <div>
                        <label class="block text-xs font-medium text-slate-600 mb-1.5">Poste / Titre</label>
                        <input type="text" name="poste"
                               class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                               placeholder="Développeur Frontend">
                    </div>
                    <div>
                        <label class="block text-xs font-medium text-slate-600 mb-1.5">Entreprise</label>
                        <input type="text" name="entreprise"
                               class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                               placeholder="Nom de l'entreprise">
                    </div>
                </div>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-4">
                    <div>
                        <label class="block text-xs font-medium text-slate-600 mb-1.5">Date de début</label>
                        <input type="month" name="dateDebut"
                               class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                    </div>
                    <div>
                        <label class="block text-xs font-medium text-slate-600 mb-1.5">Date de fin <span class="text-slate-400">(vide = en cours)</span></label>
                        <input type="month" name="dateFin"
                               class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                    </div>
                </div>
                <div>
                    <label class="block text-xs font-medium text-slate-600 mb-1.5">Description des missions</label>
                    <textarea name="descriptionExp" rows="2"
                              class="w-full px-3 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-800 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
                              placeholder="Décrivez vos responsabilités..."></textarea>
                </div>
            `;
            container.appendChild(div);
        }

        function removeExperience(btn) {
            const block = btn.closest('.exp-block');
            block.style.opacity = '0';
            block.style.transform = 'translateX(-10px)';
            block.style.transition = 'all 0.2s ease';
            setTimeout(() => block.remove(), 200);
        }
    </script>
</body>
</html>
