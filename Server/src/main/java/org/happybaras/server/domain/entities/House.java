package org.happybaras.server.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "happy_house")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int numberOfHabitants;

    private String houseNumber;

    private String address;

    private String telephone;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @OneToMany(mappedBy = "house", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "house", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Permit> permits;

    @OneToMany(mappedBy = "house", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Entry> entries;
}
