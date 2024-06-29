package org.happybaras.server.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "happy_entry")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime timestamp;
    private String comment;
    private String document;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private User vigilant;

    @ManyToOne(fetch = FetchType.EAGER)
    private House house;
}
