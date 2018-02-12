package com.tourneynizer.tourneynizer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.KeyGenerator;
import java.sql.Timestamp;
import java.util.regex.Pattern;

public class User {
    private Long id;
    private String email, name, hashedPassword;
    private Timestamp timeCreated;

    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //https://stackoverflow.com/questions/8204680/java-regex-email
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public User(String email, String name, String hashedPassword) {
        setEmail(email);
        setName(name);
        this.hashedPassword = hashedPassword;
    }

    public User(Long id, String email, String name, String hashedPassword, Timestamp timeCreated) {
        this(email, name, hashedPassword);
        persist(id, timeCreated);
    }

    public void setPlaintextPassword(String password) {
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password cannot be empty");
        this.hashedPassword = passwordEncoder.encode(password);
    }

    public boolean correctPassword(String password) {
        return passwordEncoder.matches(password, this.hashedPassword);
    }

    private void setEmail(String email) {
        if (email == null) { throw new IllegalArgumentException("Email is required"); }
        if (email.length() >= 256) { throw new IllegalArgumentException("Email is too long"); }
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()) {
            throw new IllegalArgumentException("Email is invalid");
        }

        this.email = email;
    }

    private void setName(String name) {
        if (name == null || name.isEmpty()) { throw new IllegalArgumentException("Name cannot be empty"); }
        if (name.length() >= 256) { throw new IllegalArgumentException("Name is too long"); }
        this.name = name;
    }

    @JsonIgnore
    public boolean isPersisted() {
        return id != null;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void persist(Long id, Timestamp timeCreated) {
        if (id == null) { throw new IllegalArgumentException("id cannot be null"); }
        if (timeCreated == null) { throw new IllegalArgumentException("timeCreated cannot be null"); }
        this.id = id;
        this.timeCreated = timeCreated;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public Long getId() {
        return id;
    }

    private boolean equalsHelper(Object o1, Object o2) {
        if (o1 == null && o2 == null) return true;
        if (o1 == null) return false;
        return o1.equals(o2);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User o = (User) other;

            return equalsHelper(this.id, o.id) &&
                    equalsHelper(this.email, o.email) &&
                    equalsHelper(this.name, o.name) &&
                    // Hashed passwords are salted (randomly), we will not compare them
                    equalsHelper(this.timeCreated, o.timeCreated);
        }
        return false;
    }

    @Override
    public String toString() {
        return "id: " + id + "\n" +
                "email: " + email + "\n" +
                "name: " + name + "\n" +
                "hashedPassword: " + hashedPassword + "\n" +
                "timeCreated: " + timeCreated + "\n";
    }
}
