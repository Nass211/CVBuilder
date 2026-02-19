package com.cvbuilder.util;

import com.cvbuilder.model.CV;
import com.cvbuilder.model.User;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UTILITY - Handles all file reading and writing.
 *
 * We use .txt files for storage. Here's how files are organized:
 *
 * /dataDir/
 *   users.txt          → one user per line: username|password|email
 *   cvs/
 *     cv_abc123.txt    → one CV stored as key=value lines
 *
 * This is the "Model" layer that talks to our "database" (files).
 */
public class FileStorage {

    private String dataDir;

    public FileStorage(String dataDir) {
        this.dataDir = dataDir;
        // Make sure directories exist when we start
        createDirectoriesIfNeeded();
    }

    // ============================================================
    //  SETUP
    // ============================================================

    private void createDirectoriesIfNeeded() {
        try {
            Files.createDirectories(Paths.get(dataDir));
            Files.createDirectories(Paths.get(dataDir, "cvs"));
        } catch (IOException e) {
            System.err.println("Could not create data directories: " + e.getMessage());
        }
    }

    // ============================================================
    //  USER OPERATIONS
    // ============================================================

    /**
     * Saves a new user to users.txt
     * Returns false if username already exists
     */
    public boolean saveUser(User user) {
        // First check if username exists
        if (findUser(user.getUsername()) != null) {
            return false; // Username taken
        }

        Path usersFile = Paths.get(dataDir, "users.txt");
        try {
            // Append a new line to users.txt
            String line = user.toFileString() + System.lineSeparator();
            Files.write(usersFile, line.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Finds a user by username. Returns null if not found.
     */
    public User findUser(String username) {
        Path usersFile = Paths.get(dataDir, "users.txt");
        if (!Files.exists(usersFile)) return null;

        try {
            List<String> lines = Files.readAllLines(usersFile);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                User user = User.fromFileString(line.trim());
                if (user != null && user.getUsername().equals(username)) {
                    return user;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users: " + e.getMessage());
        }
        return null;
    }

    /**
     * Checks username + password. Returns user if valid, null if not.
     */
    public User authenticate(String username, String password) {
        User user = findUser(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // ============================================================
    //  CV OPERATIONS
    // ============================================================

    /**
     * Saves a CV to its own file: cv_<id>.txt
     * Each field is stored as: fieldName=value
     * For lists (experiences, formations), each item is on its own line with a prefix
     */
    public void saveCV(CV cv) {
        Path cvFile = Paths.get(dataDir, "cvs", "cv_" + cv.getId() + ".txt");
        try {
            List<String> lines = new ArrayList<>();

            // Basic fields
            lines.add("id=" + safe(cv.getId()));
            lines.add("owner=" + safe(cv.getOwnerUsername()));
            lines.add("title=" + safe(cv.getTitle()));
            lines.add("template=" + cv.getTemplateChoice());
            lines.add("createdAt=" + safe(cv.getCreatedAt()));

            // Step 1 - Personal info
            lines.add("nom=" + safe(cv.getNom()));
            lines.add("prenom=" + safe(cv.getPrenom()));
            lines.add("email=" + safe(cv.getEmail()));
            lines.add("telephone=" + safe(cv.getTelephone()));
            lines.add("adresse=" + safe(cv.getAdresse()));
            lines.add("ville=" + safe(cv.getVille()));
            lines.add("dateNaissance=" + safe(cv.getDateNaissance()));
            lines.add("nationalite=" + safe(cv.getNationalite()));
            lines.add("linkedin=" + safe(cv.getLinkedin()));
            lines.add("siteWeb=" + safe(cv.getSiteWeb()));
            lines.add("resume=" + safe(cv.getResume()));

            // Step 2 - Experiences (each one on its own line, prefixed with "exp:")
            if (cv.getExperiences() != null) {
                for (String exp : cv.getExperiences()) {
                    lines.add("exp:" + exp);
                }
            }

            // Step 3 - Formations
            if (cv.getFormations() != null) {
                for (String form : cv.getFormations()) {
                    lines.add("form:" + form);
                }
            }
            lines.add("loisirs=" + safe(cv.getLoisirs()));
            lines.add("competences=" + safe(cv.getCompetences()));
            lines.add("langues=" + safe(cv.getLangues()));

            // Write all lines to file (overwrite if exists)
            Files.write(cvFile, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            System.err.println("Error saving CV: " + e.getMessage());
        }
    }

    /**
     * Reads one CV from file by its ID.
     */
    public CV loadCV(String cvId) {
        Path cvFile = Paths.get(dataDir, "cvs", "cv_" + cvId + ".txt");
        if (!Files.exists(cvFile)) return null;

        try {
            List<String> lines = Files.readAllLines(cvFile);
            CV cv = new CV();
            List<String> experiences = new ArrayList<>();
            List<String> formations = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                if (line.startsWith("exp:")) {
                    experiences.add(line.substring(4));
                } else if (line.startsWith("form:")) {
                    formations.add(line.substring(5));
                } else if (line.contains("=")) {
                    // Split only on first = (in case value contains =)
                    int idx = line.indexOf('=');
                    String key = line.substring(0, idx).trim();
                    String value = line.substring(idx + 1).trim();

                    switch (key) {
                        case "id": cv.setId(value); break;
                        case "owner": cv.setOwnerUsername(value); break;
                        case "title": cv.setTitle(value); break;
                        case "template": cv.setTemplateChoice(Integer.parseInt(value.isEmpty() ? "1" : value)); break;
                        case "createdAt": cv.setCreatedAt(value); break;
                        case "nom": cv.setNom(value); break;
                        case "prenom": cv.setPrenom(value); break;
                        case "email": cv.setEmail(value); break;
                        case "telephone": cv.setTelephone(value); break;
                        case "adresse": cv.setAdresse(value); break;
                        case "ville": cv.setVille(value); break;
                        case "dateNaissance": cv.setDateNaissance(value); break;
                        case "nationalite": cv.setNationalite(value); break;
                        case "linkedin": cv.setLinkedin(value); break;
                        case "siteWeb": cv.setSiteWeb(value); break;
                        case "resume": cv.setResume(value); break;
                        case "loisirs": cv.setLoisirs(value); break;
                        case "competences": cv.setCompetences(value); break;
                        case "langues": cv.setLangues(value); break;
                    }
                }
            }

            cv.setExperiences(experiences);
            cv.setFormations(formations);
            return cv;

        } catch (IOException e) {
            System.err.println("Error loading CV: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets all CVs that belong to a specific user.
     */
    public List<CV> getCVsForUser(String username) {
        List<CV> result = new ArrayList<>();
        Path cvsDir = Paths.get(dataDir, "cvs");

        try {
            Files.list(cvsDir)
                .filter(p -> p.getFileName().toString().startsWith("cv_"))
                .forEach(p -> {
                    String filename = p.getFileName().toString();
                    String id = filename.replace("cv_", "").replace(".txt", "");
                    CV cv = loadCV(id);
                    if (cv != null && username.equals(cv.getOwnerUsername())) {
                        result.add(cv);
                    }
                });
        } catch (IOException e) {
            System.err.println("Error listing CVs: " + e.getMessage());
        }

        return result;
    }

    /**
     * Deletes a CV file.
     */
    public boolean deleteCV(String cvId) {
        Path cvFile = Paths.get(dataDir, "cvs", "cv_" + cvId + ".txt");
        try {
            return Files.deleteIfExists(cvFile);
        } catch (IOException e) {
            return false;
        }
    }

    // Helper: makes sure a null value becomes empty string (no "null" in files)
    private String safe(String value) {
        return value == null ? "" : value;
    }
}
