package com.cvbuilder.service;

import com.cvbuilder.dao.UserDAO;
import com.cvbuilder.model.User;

/**
 * SERVICE — UserService
 *
 * La couche Service contient la LOGIQUE MÉTIER.
 * Elle fait le lien entre les Servlets et les DAOs.
 *
 * Pourquoi une couche Service ?
 * → Les Servlets ne savent pas comment valider un user (c'est le Service qui sait)
 * → Le DAO ne sait pas ce qu'est une "règle métier" (il lit/écrit juste)
 * → Le Service orchestre : valide, appelle le DAO, retourne un résultat propre
 *
 * Exemple du flux pour l'inscription :
 *   RegisterServlet → UserService.register() → UserDAO.save()
 */
public class UserService {

    // Le Service utilise l'INTERFACE UserDAO, pas l'implémentation concrète.
    // → Si on change d'implémentation DAO, le Service ne change pas.
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Inscrit un nouvel utilisateur après validation.
     *
     * Règles métier :
     * - Username minimum 3 caractères
     * - Password minimum 4 caractères
     * - Email ne doit pas être vide
     * - Username ne doit pas déjà exister
     *
     * @return null si succès, message d'erreur si échec
     */
    public String register(String username, String password, String email) {
        // Validation des champs
        if (username == null || username.trim().length() < 3) {
            return "Le nom d'utilisateur doit avoir au moins 3 caractères.";
        }
        if (password == null || password.trim().length() < 4) {
            return "Le mot de passe doit avoir au moins 4 caractères.";
        }
        if (email == null || email.trim().isEmpty()) {
            return "L'email est obligatoire.";
        }

        // Délègue la sauvegarde au DAO
        User newUser = new User(username.trim(), password, email.trim());
        boolean saved = userDAO.save(newUser);

        if (!saved) {
            return "Ce nom d'utilisateur est déjà pris. Choisissez-en un autre.";
        }
        return null; // null = succès
    }

    /**
     * Authentifie un utilisateur.
     *
     * @return l'User si les identifiants sont corrects, null sinon
     */
    public User login(String username, String password) {
        if (username == null || password == null) return null;
        return userDAO.authenticate(username.trim(), password);
    }
}
