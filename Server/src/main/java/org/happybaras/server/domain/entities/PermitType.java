package org.happybaras.server.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "happy_permit_type")
public class PermitType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String type;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Permit> permits;
}
