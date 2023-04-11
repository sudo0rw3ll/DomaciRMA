package com.dvvee.dnevnjakapp.model;

public class User {

    private Long id;
    private String email;
    private String username;
    private String password;
    private String imageLink;

    public User(Long id, String email, String username, String password, String imageLink){
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.imageLink = imageLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
