package com.cvbuilder.model;

/**
 * MODEL - Represents a User in our system.
 * This is just a simple Java class (POJO) to hold user data.
 */
public class User {
    private String username;
    private String password; // In real apps, you'd hash this!
    private String email;

    // Default constructor needed for some operations
    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    /**
     * Converts user to a line of text for file storage.
     * Format: username|password|email
     */
    public String toFileString() {
        return username + "|" + password + "|" + email;
    }

    /**
     * Creates a User from a line of text read from file.
     */
    public static User fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length >= 3) {
            return new User(parts[0], parts[1], parts[2]);
        }
        return null;
    }
}
