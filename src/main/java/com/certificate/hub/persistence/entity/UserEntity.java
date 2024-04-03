package com.certificate.hub.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @JsonIgnore
    @Column(nullable = false, length = 200)
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles rol;

    @Column(nullable = false, length = 50)
    private String email;

    @JsonIgnore
    @Column(nullable = false, columnDefinition = "TINYINT")
    @ColumnDefault("0")
    @Builder.Default
    private Boolean locked = false;

    @JsonIgnore
    @Column(nullable = false, columnDefinition = "TINYINT")
    @ColumnDefault("0")
    @Builder.Default
    private Boolean disabled = false;
}