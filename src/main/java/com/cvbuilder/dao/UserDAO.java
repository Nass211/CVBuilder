package com.cvbuilder.dao;

import com.cvbuilder.model.User;

/**
 * DAO INTERFACE — UserDAO
 *
 * Le patron DAO (Data Access Object) sépare complètement
 * la LOGIQUE MÉTIER de l'ACCÈS AUX DONNÉES.
 *
 * Cette interface définit les opérations possibles sur les utilisateurs.
 * Elle ne sait PAS comment les données sont stockées (fichier ? BDD ? RAM ?).
 *
 * Avantage : si demain on veut passer de fichiers .txt à MySQL,
 * on crée juste une nouvelle implémentation de cette interface.
 * Le reste du code (Servlets, Services) ne change PAS.
 */
public interface UserDAO {

    /**
     * Sauvegarde un nouvel utilisateur.
     * @return true si sauvegardé, false si le username existe déjà
     */
    boolean save(User user);

    /**
     * Cherche un utilisateur par son username.
     * @return l'User trouvé, ou null si inexistant
     */
    User findByUsername(String username);

    /**
     * Vérifie les identifiants de connexion.
     * @return l'User si les identifiants sont corrects, null sinon
     */
    User authenticate(String username, String password);
}
