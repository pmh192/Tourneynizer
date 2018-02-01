package com.dreamteam.tourneynizer.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        this(null, email, name, hashedPassword, null);
    }

    public User(Long id, String email, String name, String hashedPassword, Timestamp timeCreated) {
        this.id = id;
        setEmail(email);
        setName(name);
        this.hashedPassword = hashedPassword;
        this.timeCreated = timeCreated;
    }

    public void setPlaintextPassword(String password) {
        this.hashedPassword = passwordEncoder.encode(password);
    }

    public boolean correctPassword(String password) {
        return passwordEncoder.matches(password, this.hashedPassword);
    }

    private void setEmail(String email) {
        if (email.length() >= 256) { throw new IllegalArgumentException("Email is too long"); }
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()) {
            throw new IllegalArgumentException("Email is invalid");
        }

        this.email = email;
    }

    private void setName(String name) {
        if (name.length() >= 256) { throw new IllegalArgumentException("Name is too long"); }
        if (name.isEmpty()) { throw new IllegalArgumentException("Name cannot be empty"); }
        this.name = name;
    }
}
