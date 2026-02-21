package com.cvbuilder.dao;

import com.cvbuilder.model.User;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * IMPLÉMENTATION DAO — FileSystemUserDAO
 *
 * Implémente UserDAO en utilisant un fichier texte (users.txt).
 *
 * C'est ICI que se trouve tout le code qui touche aux fichiers.
 * Les Servlets et Services ne voient jamais ce code —
 * ils n'utilisent que l'interface UserDAO.
 *
 * Format du fichier users.txt :
 *   username|password|email
 *   alice|motdepasse|alice@email.com
 *   bob|secret|bob@email.com
 */
public class FileSystemUserDAO implements UserDAO {

    // Chemin complet vers users.txt
    private final Path usersFile;

    public FileSystemUserDAO(String dataDir) {
        this.usersFile = Paths.get(dataDir, "users.txt");
        // S'assure que le dossier parent existe
        try {
            Files.createDirectories(usersFile.getParent());
        } catch (IOException e) {
            System.err.println("[UserDAO] Impossible de créer le dossier : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // save() — écrit une nouvelle ligne dans users.txt
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public boolean save(User user) {
        // Vérifier que le username n'existe pas déjà
        if (findByUsername(user.getUsername()) != null) {
            return false; // Username déjà pris
        }
        try {
            String line = user.toFileString() + System.lineSeparator();
            Files.write(usersFile, line.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            System.err.println("[UserDAO] Erreur écriture : " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // findByUsername() — lit users.txt ligne par ligne
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public User findByUsername(String username) {
        if (!Files.exists(usersFile)) return null;
        try {
            List<String> lines = Files.readAllLines(usersFile);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                User u = User.fromFileString(line.trim());
                if (u != null && u.getUsername().equals(username)) {
                    return u;
                }
            }
        } catch (IOException e) {
            System.err.println("[UserDAO] Erreur lecture : " + e.getMessage());
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // authenticate() — délègue à findByUsername puis compare le mot de passe
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public User authenticate(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
