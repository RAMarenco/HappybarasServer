package org.happybaras.server.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "happy_permit")
public class Permit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime timestamp;
    private LocalTime beginHour;
    private LocalTime endHour;

    @ManyToOne(fetch = FetchType.EAGER)
    private House house;

    @ManyToOne(fetch = FetchType.EAGER)
    private User resident;

    @ManyToOne(fetch = FetchType.EAGER)
    private User visitor;

    @ManyToOne(fetch = FetchType.EAGER)
    private PermitType type;

    private String period;

    @ManyToOne(fetch = FetchType.EAGER)
    private PermitStatus status;

    @OneToMany(mappedBy = "permit", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PermitDay> permitDays;
}
