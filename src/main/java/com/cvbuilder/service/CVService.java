package com.cvbuilder.service;

import com.cvbuilder.dao.CVDAO;
import com.cvbuilder.model.CV;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE — CVService
 *
 * Contient toute la logique métier liée aux CVs.
 * Les Servlets appellent ce service — elles n'appellent JAMAIS le DAO directement.
 *
 * Flux DAO complet :
 *   Servlet → CVService → CVDAO (interface) → FileSystemCVDAO (implémentation)
 *
 * Le Service gère :
 * - La création d'un nouveau CV avec un ID unique
 * - La mise à jour des différentes étapes
 * - La validation des données
 * - La vérification que l'utilisateur est bien le propriétaire
 */
public class CVService {

    private final CVDAO cvDAO;

    public CVService(CVDAO cvDAO) {
        this.cvDAO = cvDAO;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ÉTAPE 1 : État Civil
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Crée ou met à jour un CV avec les infos de l'étape 1.
     * Si le CV existe (modification), on le charge d'abord.
     * Sinon on en crée un nouveau.
     *
     * @param existingCV le CV en cours (depuis session), ou null si nouveau
     * @param username   le propriétaire
     * @param params     les données du formulaire (title, nom, prenom, ...)
     * @return le CV mis à jour et sauvegardé
     */
    public CV saveStep1(CV existingCV, String username, CVFormParams params) {
        CV cv = (existingCV != null) ? existingCV : new CV();

        // Si nouveau CV, initialiser le propriétaire et la date
        if (existingCV == null || cv.getOwnerUsername() == null) {
            cv.setOwnerUsername(username);
            cv.setCreatedAt(LocalDate.now().toString());
        }

        // Remplir les données de l'étape 1
        cv.setTitle(params.title);
        cv.setNom(params.nom);
        cv.setPrenom(params.prenom);
        cv.setEmail(params.email);
        cv.setTelephone(params.telephone);
        cv.setAdresse(params.adresse);
        cv.setVille(params.ville);
        cv.setDateNaissance(params.dateNaissance);
        cv.setNationalite(params.nationalite);
        cv.setLinkedin(params.linkedin);
        cv.setSiteWeb(params.siteWeb);
        cv.setResume(params.resume);

        // Persiste via le DAO
        cvDAO.save(cv);
        return cv;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ÉTAPE 2 : Expériences
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Met à jour les expériences d'un CV existant.
     * Chaque expérience est encodée en : "poste|entreprise|debut|fin|description"
     */
    public CV saveStep2(CV cv, String[] postes, String[] entreprises,
                        String[] dateDebuts, String[] dateFins, String[] descriptions) {

        List<String> experiences = new ArrayList<>();

        if (postes != null) {
            for (int i = 0; i < postes.length; i++) {
                String poste = postes[i] != null ? postes[i].trim() : "";
                if (poste.isEmpty()) continue; // ignorer les entrées vides

                String entreprise = getAt(entreprises, i);
                String debut      = getAt(dateDebuts, i);
                String fin        = getAt(dateFins, i);
                String desc       = getAt(descriptions, i);

                // Encode en une seule ligne séparée par |
                experiences.add(
                    clean(poste) + "|" + clean(entreprise) + "|" +
                    clean(debut) + "|" + clean(fin) + "|" + clean(desc)
                );
            }
        }

        cv.setExperiences(experiences);
        cvDAO.save(cv);
        return cv;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ÉTAPE 3 : Formation & Loisirs
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Met à jour les formations, compétences, langues et loisirs d'un CV.
     */
    public CV saveStep3(CV cv, String[] diplomes, String[] ecoles,
                        String[] annees, String[] descriptions,
                        String loisirs, String competences, String langues) {

        List<String> formations = new ArrayList<>();

        if (diplomes != null) {
            for (int i = 0; i < diplomes.length; i++) {
                String diplome = diplomes[i] != null ? diplomes[i].trim() : "";
                if (diplome.isEmpty()) continue;

                String ecole = getAt(ecoles, i);
                String annee = getAt(annees, i);
                String desc  = getAt(descriptions, i);

                formations.add(
                    clean(diplome) + "|" + clean(ecole) + "|" +
                    clean(annee) + "|" + clean(desc)
                );
            }
        }

        cv.setFormations(formations);
        cv.setLoisirs(loisirs);
        cv.setCompetences(competences);
        cv.setLangues(langues);

        cvDAO.save(cv);
        return cv;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEMPLATE
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Enregistre le choix de template (1, 2 ou 3).
     */
    public CV saveTemplate(CV cv, int templateChoice) {
        if (templateChoice < 1 || templateChoice > 3) templateChoice = 1;
        cv.setTemplateChoice(templateChoice);
        cvDAO.save(cv);
        return cv;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LECTURE & SUPPRESSION
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Charge un CV par ID — vérifie que l'utilisateur en est bien le propriétaire.
     * @return le CV, ou null si introuvable ou accès non autorisé
     */
    public CV findByIdForUser(String cvId, String username) {
        CV cv = cvDAO.findById(cvId);
        if (cv == null) return null;
        // Vérification de propriété (sécurité)
        if (!cv.getOwnerUsername().equals(username)) return null;
        return cv;
    }

    /**
     * Récupère tous les CVs d'un utilisateur.
     */
    public List<CV> getAllForUser(String username) {
        return cvDAO.findByOwner(username);
    }

    /**
     * Supprime un CV — vérifie la propriété avant de supprimer.
     */
    public boolean delete(String cvId, String username) {
        CV cv = cvDAO.findById(cvId);
        if (cv == null || !cv.getOwnerUsername().equals(username)) {
            return false; // Pas propriétaire → refus
        }
        return cvDAO.delete(cvId);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPERS PRIVÉS
    // ─────────────────────────────────────────────────────────────────────────

    private String getAt(String[] arr, int i) {
        if (arr == null || i >= arr.length || arr[i] == null) return "";
        return arr[i].trim();
    }

    // Supprime le | des valeurs pour ne pas corrompre le format fichier
    private String clean(String s) {
        if (s == null) return "";
        return s.replace("|", " ").replace("\n", " ").replace("\r", "");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CLASSE INTERNE : CVFormParams (pour passer les données step1 proprement)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Simple conteneur pour les paramètres du formulaire Step 1.
     * Évite une méthode avec 12 paramètres !
     */
    public static class CVFormParams {
        public String title, nom, prenom, email, telephone;
        public String adresse, ville, dateNaissance, nationalite;
        public String linkedin, siteWeb, resume;
    }
}
