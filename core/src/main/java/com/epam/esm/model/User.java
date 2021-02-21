package com.epam.esm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "users_seq")
    @SequenceGenerator(name="users_seq",
            sequenceName="users_id_seq", allocationSize=1)
    private Long id;

    private String name;

    private String surname;
}
