<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription – CV Builder</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');
        * { font-family: 'Inter', sans-serif; }
    </style>
</head>
<body class="min-h-screen bg-slate-50 flex items-center justify-center p-4">

    <div class="fixed inset-0 overflow-hidden pointer-events-none">
        <div class="absolute -top-40 -right-40 w-80 h-80 bg-blue-100 rounded-full opacity-60"></div>
        <div class="absolute -bottom-40 -left-40 w-96 h-96 bg-indigo-100 rounded-full opacity-60"></div>
    </div>

    <div class="relative w-full max-w-md">
        <!-- Brand -->
        <div class="text-center mb-8">
            <div class="inline-flex items-center justify-center w-16 h-16 bg-blue-600 rounded-2xl mb-4">
                <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                </svg>
            </div>
            <h1 class="text-2xl font-bold text-slate-800">CV Builder</h1>
            <p class="text-slate-500 text-sm mt-1">Commencez à créer votre CV</p>
        </div>

        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-8">
            <h2 class="text-xl font-semibold text-slate-800 mb-6">Créer un compte</h2>

            <% if (request.getAttribute("error") != null) { %>
            <div class="bg-red-50 border border-red-200 text-red-700 rounded-xl p-4 mb-5 text-sm flex items-center gap-2">
                <svg class="w-4 h-4 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/>
                </svg>
                <%= request.getAttribute("error") %>
            </div>
            <% } %>

            <form action="<%= request.getContextPath() %>/register" method="POST" class="space-y-4">
                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Nom d'utilisateur
                    </label>
                    <input type="text" name="username" required minlength="3"
                           value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>"
                           class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400
                                  focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                           placeholder="Minimum 3 caractères">
                </div>

                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Email
                    </label>
                    <input type="email" name="email" required
                           value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>"
                           class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400
                                  focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                           placeholder="votre@email.com">
                </div>

                <div>
                    <label class="block text-sm font-medium text-slate-700 mb-2">
                        Mot de passe
                    </label>
                    <input type="password" name="password" required minlength="4"
                           class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50 text-slate-800 placeholder-slate-400
                                  focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                           placeholder="Choisissez un mot de passe">
                </div>

                <button type="submit"
                        class="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6
                               rounded-xl transition-colors duration-200 mt-2">
                    Créer mon compte
                </button>
            </form>
        </div>

        <p class="text-center text-slate-500 text-sm mt-6">
            Déjà un compte ?
            <a href="<%= request.getContextPath() %>/login"
               class="text-blue-600 font-medium hover:text-blue-700">
                Se connecter
            </a>
        </p>
    </div>

</body>
</html>
