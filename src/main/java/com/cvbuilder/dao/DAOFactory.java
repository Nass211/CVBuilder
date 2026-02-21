package com.cvbuilder.dao;

import jakarta.servlet.ServletContext;

/**
 * DAO FACTORY
 *
 * La Factory est responsable de créer et fournir les instances DAO.
 *
 * Pourquoi une Factory ?
 * → Centralise la création des DAOs
 * → Une seule instance de chaque DAO pour toute l'application (singleton)
 * → Si on veut changer d'implémentation (ex: BDD au lieu de fichiers),
 *   on change SEULEMENT cette classe, rien d'autre.
 *
 * Utilisation dans les Servlets :
 *   UserDAO userDAO = DAOFactory.getUserDAO(getServletContext());
 *   CVDAO   cvDAO   = DAOFactory.getCVDAO(getServletContext());
 */
public class DAOFactory {

    // Instances uniques (pattern Singleton)
    private static UserDAO userDAO;
    private static CVDAO   cvDAO;

    /**
     * Retourne l'instance unique de UserDAO.
     * La crée si c'est le premier appel.
     */
    public static synchronized UserDAO getUserDAO(ServletContext context) {
        if (userDAO == null) {
            String dataDir = getDataDir(context);
            userDAO = new FileSystemUserDAO(dataDir);
            System.out.println("[DAOFactory] UserDAO initialisé → " + dataDir);
        }
        return userDAO;
    }

    /**
     * Retourne l'instance unique de CVDAO.
     * La crée si c'est le premier appel.
     */
    public static synchronized CVDAO getCVDAO(ServletContext context) {
        if (cvDAO == null) {
            String dataDir = getDataDir(context);
            cvDAO = new FileSystemCVDAO(dataDir);
            System.out.println("[DAOFactory] CVDAO initialisé → " + dataDir);
        }
        return cvDAO;
    }

    /**
     * Lit le dossier de données depuis web.xml (context-param "dataDir").
     * Si non configuré, utilise /tmp/cvbuilder-data par défaut.
     */
    private static String getDataDir(ServletContext context) {
        String dataDir = context.getInitParameter("dataDir");
        if (dataDir == null || dataDir.trim().isEmpty()) {
            dataDir = System.getProperty("java.io.tmpdir") + "/cvbuilder-data";
        }
        return dataDir;
    }
}
