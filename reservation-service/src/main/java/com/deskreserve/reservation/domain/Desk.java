package com.deskreserve.reservation.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "desks")
public class Desk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private boolean active = true;

    protected Desk() {}

    public Desk(String name, String location) {
        this.name = name;
        this.location = location;
        this.active = true;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public boolean isActive() { return active; }

    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setActive(boolean active) { this.active = active; }
}
