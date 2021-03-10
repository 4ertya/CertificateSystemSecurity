package com.epam.esm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor

@Table(name = "tags")
public class Tag{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "tags_seq")
    @SequenceGenerator(name="tags_seq",
            sequenceName="tags_id_seq", allocationSize=1)
    private Long id;
    @Column(unique = true)
    private String name;
}
