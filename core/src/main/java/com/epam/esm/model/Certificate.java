package com.epam.esm.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "certificates_seq")
    @SequenceGenerator(name="certificates_seq",
            sequenceName="certificates_id_seq", allocationSize=1)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "certificates_tags",
            joinColumns = {@JoinColumn(name = "certificates_id")},
            inverseJoinColumns = {@JoinColumn(name = "tags_id")})

    private Set<Tag> tags = new HashSet<>();
    }