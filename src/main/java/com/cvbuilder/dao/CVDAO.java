package com.cvbuilder.dao;

import com.cvbuilder.model.CV;
import java.util.List;

/**
 * DAO INTERFACE — CVDAO
 *
 * Définit toutes les opérations CRUD possibles sur les CVs.
 * CRUD = Create, Read, Update, Delete
 *
 * L'interface ne contient que des signatures de méthodes.
 * C'est l'IMPLÉMENTATION (FileSystemCVDAO) qui contient
 * le vrai code d'accès aux fichiers .txt.
 */
public interface CVDAO {

    /**
     * Sauvegarde ou met à jour un CV (si l'ID existe déjà, on écrase).
     */
    void save(CV cv);

    /**
     * Récupère un CV par son ID unique.
     * @return le CV trouvé, ou null si inexistant
     */
    CV findById(String id);

    /**
     * Récupère tous les CVs appartenant à un utilisateur.
     */
    List<CV> findByOwner(String username);

    /**
     * Supprime un CV par son ID.
     * @return true si supprimé, false si non trouvé
     */
    boolean delete(String id);
}
