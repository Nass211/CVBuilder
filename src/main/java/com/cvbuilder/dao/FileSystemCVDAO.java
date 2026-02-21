package com.cvbuilder.dao;

import com.cvbuilder.model.CV;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * IMPLÉMENTATION DAO — FileSystemCVDAO
 *
 * Implémente CVDAO en utilisant des fichiers texte.
 * Chaque CV est stocké dans son propre fichier : cv_XXXXXXXX.txt
 *
 * Format du fichier :
 *   clé=valeur      (pour les champs simples)
 *   exp:...         (pour chaque expérience)
 *   form:...        (pour chaque formation)
 *
 * Les Servlets n'ont aucune idée que les données viennent de fichiers.
 * Ils appellent juste cvDAO.save(cv), cvDAO.findById(id), etc.
 */
public class FileSystemCVDAO implements CVDAO {

    private final Path cvsDir;

    public FileSystemCVDAO(String dataDir) {
        this.cvsDir = Paths.get(dataDir, "cvs");
        try {
            Files.createDirectories(cvsDir);
        } catch (IOException e) {
            System.err.println("[CVDAO] Impossible de créer le dossier cvs : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // save() — écrit toutes les données du CV dans cv_ID.txt
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public void save(CV cv) {
        Path cvFile = cvsDir.resolve("cv_" + cv.getId() + ".txt");
        try {
            List<String> lines = new ArrayList<>();

            // Champs simples → format clé=valeur
            lines.add("id="           + safe(cv.getId()));
            lines.add("owner="        + safe(cv.getOwnerUsername()));
            lines.add("title="        + safe(cv.getTitle()));
            lines.add("template="     + cv.getTemplateChoice());
            lines.add("createdAt="    + safe(cv.getCreatedAt()));
            lines.add("nom="          + safe(cv.getNom()));
            lines.add("prenom="       + safe(cv.getPrenom()));
            lines.add("email="        + safe(cv.getEmail()));
            lines.add("telephone="    + safe(cv.getTelephone()));
            lines.add("adresse="      + safe(cv.getAdresse()));
            lines.add("ville="        + safe(cv.getVille()));
            lines.add("dateNaissance="+ safe(cv.getDateNaissance()));
            lines.add("nationalite="  + safe(cv.getNationalite()));
            lines.add("linkedin="     + safe(cv.getLinkedin()));
            lines.add("siteWeb="      + safe(cv.getSiteWeb()));
            lines.add("resume="       + safe(cv.getResume()));
            lines.add("loisirs="      + safe(cv.getLoisirs()));
            lines.add("competences="  + safe(cv.getCompetences()));
            lines.add("langues="      + safe(cv.getLangues()));

            // Expériences → une ligne par expérience, préfixée "exp:"
            if (cv.getExperiences() != null) {
                for (String exp : cv.getExperiences()) {
                    lines.add("exp:" + exp);
                }
            }

            // Formations → une ligne par formation, préfixée "form:"
            if (cv.getFormations() != null) {
                for (String form : cv.getFormations()) {
                    lines.add("form:" + form);
                }
            }

            // Écrit (ou écrase si le fichier existe déjà)
            Files.write(cvFile, lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            System.err.println("[CVDAO] Erreur save : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // findById() — lit cv_ID.txt et reconstruit un objet CV
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public CV findById(String id) {
        Path cvFile = cvsDir.resolve("cv_" + id + ".txt");
        if (!Files.exists(cvFile)) return null;

        try {
            List<String> lines = Files.readAllLines(cvFile);
            CV cv = new CV();
            List<String> experiences = new ArrayList<>();
            List<String> formations  = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                if (line.startsWith("exp:")) {
                    // C'est une expérience
                    experiences.add(line.substring(4));

                } else if (line.startsWith("form:")) {
                    // C'est une formation
                    formations.add(line.substring(5));

                } else if (line.contains("=")) {
                    // Champ simple clé=valeur
                    int idx   = line.indexOf('=');
                    String key = line.substring(0, idx).trim();
                    String val = line.substring(idx + 1).trim();

                    switch (key) {
                        case "id":           cv.setId(val); break;
                        case "owner":        cv.setOwnerUsername(val); break;
                        case "title":        cv.setTitle(val); break;
                        case "template":
                            try { cv.setTemplateChoice(Integer.parseInt(val)); }
                            catch (NumberFormatException e) { cv.setTemplateChoice(1); }
                            break;
                        case "createdAt":    cv.setCreatedAt(val); break;
                        case "nom":          cv.setNom(val); break;
                        case "prenom":       cv.setPrenom(val); break;
                        case "email":        cv.setEmail(val); break;
                        case "telephone":    cv.setTelephone(val); break;
                        case "adresse":      cv.setAdresse(val); break;
                        case "ville":        cv.setVille(val); break;
                        case "dateNaissance":cv.setDateNaissance(val); break;
                        case "nationalite":  cv.setNationalite(val); break;
                        case "linkedin":     cv.setLinkedin(val); break;
                        case "siteWeb":      cv.setSiteWeb(val); break;
                        case "resume":       cv.setResume(val); break;
                        case "loisirs":      cv.setLoisirs(val); break;
                        case "competences":  cv.setCompetences(val); break;
                        case "langues":      cv.setLangues(val); break;
                    }
                }
            }

            cv.setExperiences(experiences);
            cv.setFormations(formations);
            return cv;

        } catch (IOException e) {
            System.err.println("[CVDAO] Erreur findById : " + e.getMessage());
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // findByOwner() — parcourt tous les fichiers cv_*.txt du dossier
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public List<CV> findByOwner(String username) {
        List<CV> result = new ArrayList<>();
        try {
            Files.list(cvsDir)
                .filter(p -> p.getFileName().toString().startsWith("cv_")
                          && p.getFileName().toString().endsWith(".txt"))
                .forEach(p -> {
                    // Extrait l'ID depuis le nom de fichier : "cv_ab12cd.txt" → "ab12cd"
                    String filename = p.getFileName().toString();
                    String id = filename.replace("cv_", "").replace(".txt", "");
                    CV cv = findById(id);
                    if (cv != null && username.equals(cv.getOwnerUsername())) {
                        result.add(cv);
                    }
                });
        } catch (IOException e) {
            System.err.println("[CVDAO] Erreur findByOwner : " + e.getMessage());
        }
        return result;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // delete() — supprime le fichier cv_ID.txt
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public boolean delete(String id) {
        Path cvFile = cvsDir.resolve("cv_" + id + ".txt");
        try {
            return Files.deleteIfExists(cvFile);
        } catch (IOException e) {
            System.err.println("[CVDAO] Erreur delete : " + e.getMessage());
            return false;
        }
    }

    // Évite d'écrire "null" dans les fichiers
    private String safe(String s) {
        return s == null ? "" : s;
    }
}
