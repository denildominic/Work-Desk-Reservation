package com.deskreserve.reservation.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "employees", indexes = @Index(name = "idx_employees_username", columnList = "username", unique = true))
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String username;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    protected Employee() {}

    public Employee(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
