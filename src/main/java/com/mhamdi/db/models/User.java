package com.mhamdi.db.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mhamdi.brander.apis.dto.response.DataInterface;
import com.mhamdi.db.models.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails, DataInterface, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // @Column(name = "id", nullable = false, columnDefinition = "NUMERIC(19,0)")
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
//    @JsonIgnore
    @Column(nullable = false)
    private String password;
    @JsonIgnore
    @ManyToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "role_id"), joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;
    @Enumerated(EnumType.STRING)
    private AccountType role;

    // @ManyToMany
    // @JoinTable(
    // name = "users_templates",
    // joinColumns = @JoinColumn(name = "user_id"),
    // inverseJoinColumns = @JoinColumn(name = "template_id"))
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    Set<UserTemplates> templates = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        // email in our case
        return "";
    }

    @Override
    public String getUsername() {
        // email in our case
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}