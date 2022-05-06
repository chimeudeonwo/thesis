package com.bepastem.models;

import com.bepastem.util.IdsGenerator;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name="USERS")
public class Users {
    @Id
    private long userId;
    @Column(unique = true, nullable = false, length = 64)
    private String username;
    @Column(nullable = false, length = 64)
    private String password;
    @Column(nullable = false, length = 64)
    private String email;
    @Column(nullable = false, length = 64)
    private String WHO;
    @Column(nullable = false)
    private boolean agreedToUserDataTerms;
    @Column(nullable = false)
    private boolean deleted;
    @Column(nullable = false,  length = 64)
    private LocalDate createdOn;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn, //(name = "user_id"),
            inverseJoinColumns = @JoinColumn //(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    @Transient
    private IdsGenerator idsGenerator = new IdsGenerator();

    public Users() throws NoSuchAlgorithmException {
        this.userId = this.idsGenerator.generateLongId();
        this.createdOn = LocalDate.now();
        this.deleted = false;
    }

    public long getUserId() {
        return userId;
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

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWHO() {
        return WHO;
    }

    public void setWHO(String WHO) {
        this.WHO = WHO;
    }

    public boolean isAgreedToUserDataTerms() {
        return agreedToUserDataTerms;
    }

    public void setAgreedToUserDataTerms(boolean agreedToUserDataTerms) {
        this.agreedToUserDataTerms = agreedToUserDataTerms;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", WHO='" + WHO + '\'' +
                ", agreedToUserDataTerms=" + agreedToUserDataTerms +
                ", deleted=" + deleted +
                ", createdOn=" + createdOn +
                ", roles=" + roles +
                '}';
    }
}
