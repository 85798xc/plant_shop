package com.example.plant_shop.model.role;

import com.example.plant_shop.model.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private ERole roleName;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    public Role(ERole roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && roleName == role.roleName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName);
    }
}
