package com.cvbuilder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * MODEL - Represents a complete CV.
 * Contains all three steps of information:
 * Step 1: Personal info (État civil)
 * Step 2: Work Experience (Expériences)
 * Step 3: Education & Hobbies (Formation & Loisirs)
 */
public class CV {

    // Unique ID for this CV (so user can have multiple CVs)
    private String id;

    // Who owns this CV
    private String ownerUsername;

    // CV title (user gives it a name like "CV Développeur")
    private String title;

    // Which template was chosen (1, 2, or 3)
    private int templateChoice;

    // ---- STEP 1: État Civil ----
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String ville;
    private String dateNaissance;
    private String nationalite;
    private String linkedin;
    private String siteWeb;
    private String resume; // Short summary about yourself

    // ---- STEP 2: Expériences ----
    // Each experience is stored as: title|company|startDate|endDate|description
    private List<String> experiences;

    // ---- STEP 3: Formation & Loisirs ----
    // Each formation: diploma|school|year|description
    private List<String> formations;
    // Loisirs as comma-separated string
    private String loisirs;
    // Skills as comma-separated string
    private String competences;
    private String langues;

    // Creation date for display on dashboard
    private String createdAt;

    public CV() {
        this.id = UUID.randomUUID().toString().substring(0, 8); // Short ID
        this.experiences = new ArrayList<>();
        this.formations = new ArrayList<>();
    }

    // ---- Getters & Setters ----

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getTemplateChoice() { return templateChoice; }
    public void setTemplateChoice(int templateChoice) { this.templateChoice = templateChoice; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getNationalite() { return nationalite; }
    public void setNationalite(String nationalite) { this.nationalite = nationalite; }

    public String getLinkedin() { return linkedin; }
    public void setLinkedin(String linkedin) { this.linkedin = linkedin; }

    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }

    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }

    public List<String> getExperiences() { return experiences; }
    public void setExperiences(List<String> experiences) { this.experiences = experiences; }

    public List<String> getFormations() { return formations; }
    public void setFormations(List<String> formations) { this.formations = formations; }

    public String getLoisirs() { return loisirs; }
    public void setLoisirs(String loisirs) { this.loisirs = loisirs; }

    public String getCompetences() { return competences; }
    public void setCompetences(String competences) { this.competences = competences; }

    public String getLangues() { return langues; }
    public void setLangues(String langues) { this.langues = langues; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // Helper: get full name
    public String getFullName() {
        return (prenom != null ? prenom : "") + " " + (nom != null ? nom : "");
    }
}
