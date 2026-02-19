<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cvbuilder.model.CV" %>
<% CV cv = (CV) request.getAttribute("cv"); %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Choisir un template – CV Builder</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
        * { font-family: 'Inter', sans-serif; }
        .template-card { transition: all 0.2s ease; cursor: pointer; }
        .template-card:hover { transform: translateY(-4px); }
        .template-card.selected { ring: 2px; }
        .preview-mini { transform: scale(0.35); transform-origin: top left; pointer-events: none; }
    </style>
</head>
<body class="min-h-screen bg-slate-50">

    <nav class="bg-white border-b border-slate-200">
        <div class="max-w-5xl mx-auto px-6 py-4 flex items-center justify-between">
            <a href="<%= request.getContextPath() %>/cv/step3" class="flex items-center gap-2 text-slate-500 hover:text-slate-700 text-sm">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
                </svg>
                Retour
            </a>
            <span class="font-semibold text-slate-800">Choisissez votre template</span>
            <div></div>
        </div>
    </nav>

    <div class="max-w-5xl mx-auto px-6 py-10">
        <div class="text-center mb-10">
            <h1 class="text-3xl font-bold text-slate-800 mb-2">Choisissez votre design</h1>
            <p class="text-slate-500">Sélectionnez le style qui vous correspond le mieux</p>
        </div>

        <form action="<%= request.getContextPath() %>/cv/template" method="POST">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">

                <!-- Template 1: Classic Blue -->
                <div class="template-card" onclick="selectTemplate(1)">
                    <div id="card1" class="border-2 border-slate-200 rounded-2xl overflow-hidden">
                        <!-- Mini CV Preview -->
                        <div class="bg-slate-50 p-4 h-64 overflow-hidden relative">
                            <div class="w-full bg-blue-600 text-white p-3 rounded-lg mb-2">
                                <div class="font-bold text-xs"><%= cv != null ? cv.getFullName() : "Prénom Nom" %></div>
                                <div class="text-blue-200 text-xs mt-0.5"><%= cv != null && cv.getTitle() != null ? cv.getTitle() : "Votre titre" %></div>
                                <div class="text-blue-200 text-xs mt-0.5"><%= cv != null && cv.getEmail() != null ? cv.getEmail() : "email@exemple.com" %></div>
                            </div>
                            <div class="space-y-2">
                                <div class="h-1.5 bg-blue-600 w-16 rounded mb-2"></div>
                                <div class="h-2 bg-slate-200 rounded w-full"></div>
                                <div class="h-2 bg-slate-200 rounded w-4/5"></div>
                                <div class="h-1.5 bg-blue-600 w-16 rounded mb-2 mt-3"></div>
                                <div class="h-2 bg-slate-200 rounded w-full"></div>
                                <div class="h-2 bg-slate-200 rounded w-3/4"></div>
                                <div class="h-2 bg-slate-200 rounded w-full"></div>
                                <div class="h-1.5 bg-blue-600 w-20 rounded mb-2 mt-3"></div>
                                <div class="h-2 bg-slate-200 rounded w-full"></div>
                                <div class="h-2 bg-slate-200 rounded w-2/3"></div>
                            </div>
                        </div>
                        <!-- Card footer -->
                        <div class="p-4 bg-white border-t border-slate-100">
                            <div class="flex items-center justify-between">
                                <div>
                                    <div class="font-semibold text-slate-800 text-sm">Classique Bleu</div>
                                    <div class="text-xs text-slate-400 mt-0.5">Professionnel & Épuré</div>
                                </div>
                                <div class="w-5 h-5 rounded-full border-2 border-slate-300 flex items-center justify-center" id="radio1">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Template 2: Modern Teal -->
                <div class="template-card" onclick="selectTemplate(2)">
                    <div id="card2" class="border-2 border-slate-200 rounded-2xl overflow-hidden">
                        <div class="bg-slate-50 p-4 h-64 overflow-hidden relative">
                            <!-- Two column layout preview -->
                            <div class="flex gap-2 h-full">
                                <div class="w-2/5 bg-teal-700 rounded-lg p-2 flex-shrink-0">
                                    <div class="w-8 h-8 bg-teal-500 rounded-full mx-auto mb-2"></div>
                                    <div class="h-2 bg-teal-500 rounded w-full mb-1"></div>
                                    <div class="h-1.5 bg-teal-600 rounded w-3/4 mb-3"></div>
                                    <div class="h-1 bg-teal-600 rounded w-2/3 mb-1"></div>
                                    <div class="h-1 bg-teal-600 rounded w-3/4 mb-1"></div>
                                    <div class="h-1 bg-teal-600 rounded w-1/2 mb-3"></div>
                                    <div class="h-1.5 bg-teal-400 rounded w-3/4 mb-1"></div>
                                    <div class="h-1 bg-teal-600 rounded w-full mb-1"></div>
                                    <div class="h-1 bg-teal-600 rounded w-4/5"></div>
                                </div>
                                <div class="flex-1 space-y-2 pt-1">
                                    <div class="h-1.5 bg-teal-600 w-12 rounded"></div>
                                    <div class="h-1.5 bg-slate-200 rounded w-full"></div>
                                    <div class="h-1.5 bg-slate-200 rounded w-4/5"></div>
                                    <div class="h-1.5 bg-slate-200 rounded w-3/4 mb-2"></div>
                                    <div class="h-1.5 bg-teal-600 w-12 rounded"></div>
                                    <div class="h-1.5 bg-slate-200 rounded w-full"></div>
                                    <div class="h-1.5 bg-slate-200 rounded w-5/6"></div>
                                    <div class="h-1.5 bg-slate-200 rounded w-full mb-2"></div>
                                    <div class="h-1.5 bg-teal-600 w-16 rounded"></div>
                                    <div class="h-1.5 bg-slate-200 rounded w-full"></div>
                                </div>
                            </div>
                        </div>
                        <div class="p-4 bg-white border-t border-slate-100">
                            <div class="flex items-center justify-between">
                                <div>
                                    <div class="font-semibold text-slate-800 text-sm">Moderne Teal</div>
                                    <div class="text-xs text-slate-400 mt-0.5">Deux colonnes & Sidebar</div>
                                </div>
                                <div class="w-5 h-5 rounded-full border-2 border-slate-300 flex items-center justify-center" id="radio2">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Template 3: Creative Purple -->
                <div class="template-card" onclick="selectTemplate(3)">
                    <div id="card3" class="border-2 border-slate-200 rounded-2xl overflow-hidden">
                        <div class="bg-slate-50 p-4 h-64 overflow-hidden relative">
                            <div class="border-l-4 border-violet-500 pl-3 mb-2">
                                <div class="font-bold text-xs text-slate-800"><%= cv != null ? cv.getFullName() : "Prénom Nom" %></div>
                                <div class="text-xs text-violet-500"><%= cv != null && cv.getTitle() != null ? cv.getTitle() : "Votre titre" %></div>
                            </div>
                            <div class="flex gap-2 text-xs text-slate-400 mb-3">
                                <span class="bg-violet-100 text-violet-600 px-1.5 py-0.5 rounded text-xs">email</span>
                                <span class="bg-violet-100 text-violet-600 px-1.5 py-0.5 rounded text-xs">tel</span>
                                <span class="bg-violet-100 text-violet-600 px-1.5 py-0.5 rounded text-xs">ville</span>
                            </div>
                            <div class="space-y-2">
                                <div class="h-1.5 bg-violet-500 w-16 rounded mb-1"></div>
                                <div class="h-2 bg-slate-200 rounded w-full"></div>
                                <div class="h-2 bg-slate-200 rounded w-4/5"></div>
                                <div class="h-1.5 bg-violet-500 w-16 rounded mb-1 mt-3"></div>
                                <div class="h-2 bg-slate-200 rounded w-full"></div>
                                <div class="h-2 bg-slate-200 rounded w-3/4"></div>
                                <div class="h-2 bg-slate-200 rounded w-full"></div>
                                <div class="h-1.5 bg-violet-500 w-20 rounded mb-1 mt-3"></div>
                                <div class="h-2 bg-slate-200 rounded w-full"></div>
                            </div>
                        </div>
                        <div class="p-4 bg-white border-t border-slate-100">
                            <div class="flex items-center justify-between">
                                <div>
                                    <div class="font-semibold text-slate-800 text-sm">Créatif Violet</div>
                                    <div class="text-xs text-slate-400 mt-0.5">Accentué & Original</div>
                                </div>
                                <div class="w-5 h-5 rounded-full border-2 border-slate-300 flex items-center justify-center" id="radio3">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Hidden input -->
            <input type="hidden" name="template" id="templateInput" value="1">

            <!-- Submit -->
            <div class="text-center">
                <button type="submit"
                        class="inline-flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold
                               px-8 py-4 rounded-xl transition-colors duration-200 text-base">
                    Générer mon CV
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                </button>
            </div>
        </form>
    </div>

    <script>
        // Default: select template 1
        selectTemplate(1);

        function selectTemplate(n) {
            // Reset all cards
            for (let i = 1; i <= 3; i++) {
                const card = document.getElementById('card' + i);
                const radio = document.getElementById('radio' + i);
                card.classList.remove('border-blue-500', 'border-teal-500', 'border-violet-500');
                card.classList.add('border-slate-200');
                radio.innerHTML = '';
                radio.classList.remove('border-blue-500', 'border-teal-500', 'border-violet-500', 'bg-blue-500', 'bg-teal-500', 'bg-violet-500');
                radio.classList.add('border-slate-300');
            }

            // Highlight selected
            const colors = {1: 'blue', 2: 'teal', 3: 'violet'};
            const color = colors[n];
            const card = document.getElementById('card' + n);
            const radio = document.getElementById('radio' + n);

            card.classList.remove('border-slate-200');
            card.classList.add('border-' + color + '-500');
            radio.classList.remove('border-slate-300');
            radio.classList.add('border-' + color + '-500', 'bg-' + color + '-500');
            radio.innerHTML = '<svg class="w-3 h-3 text-white" fill="currentColor" viewBox="0 0 20 20"><path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"/></svg>';

            document.getElementById('templateInput').value = n;
        }
    </script>
</body>
</html>
