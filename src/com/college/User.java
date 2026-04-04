package com.college;

public class User {
    private int id;
    private String username;
    private String role;
    
    // Additional identity info
    private int profileId; // maps to faculty.id, student.id, or parent.id
    private String profileName;

    public User(int id, String username, String role, int profileId, String profileName) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.profileId = profileId;
        this.profileName = profileName;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public int getProfileId() { return profileId; }
    public String getProfileName() { return profileName; }
}
