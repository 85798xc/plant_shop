package com.example.plant_shop.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@JsonIgnoreProperties(value = {"password", "isAccountNoneExpired", "isAccountNonLocked",
        "isCredentialsNonExpired", "isEnabled", "authorities"},
        allowGetters = true,
        allowSetters = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "author")
    private Set<Comment> comments;

    public User(UserDataRequest request) {
        this.username = request.getUsername();
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.roles.addAll(request.convertToRoleSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getRoleName().name()))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(avatar, user.avatar) &&
                Objects.equals(password, user.password) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, avatar, password, roles);
    }
}
