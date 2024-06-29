package org.happybaras.server.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "happy_token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "code")
    private UUID code;

    @Column(name = "content", length = 5000)
    private String content;

    @Column(name = "active")
    private boolean active;

    @Column(name = "timestamp", updatable = false)
    private Date timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_code")
    @JsonIgnore
    private User user;

    public Token(String content, User user) {
        super();
        this.content = content;
        this.user = user;
        this.timestamp = Date.from(Instant.now());
    }
}