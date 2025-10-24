package com.abcnews.entity;

import java.io.Serializable;

public class Newsletter implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;
    private boolean enabled; // Trạng thái (true-còn hiệu lực)

    // Constructor
    public Newsletter() {
    }

    public Newsletter(String email, boolean enabled) {
        this.email = email;
        this.enabled = enabled;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}