package org.happybaras.server.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "happy_user")
public class User implements UserDetails{

//    TODO: Investigar sobre la paginación desde aquí para el dashboard 
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email;
    private String username;
//    TODO: Instead of password should be something related to google auth
    @JsonIgnore
    private String password;
    private String avatar;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Token> tokens;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<House> houses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private House house;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "happy_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    private List<UserRole> roles;

    @OneToMany(mappedBy = "resident", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Permit> permitsResident;

    @OneToMany(mappedBy = "visitor", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Permit> permitsVisitor;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Entry> entries;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getId()))
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
