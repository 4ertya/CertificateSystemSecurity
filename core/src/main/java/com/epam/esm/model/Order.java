package com.epam.esm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@Audited
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "orders_seq")
    @SequenceGenerator(name="orders_seq",
            sequenceName="orders_id_seq", allocationSize=1)
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private BigDecimal cost;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "orders_certificates",
            joinColumns = {@JoinColumn(name = "orders_id")},
            inverseJoinColumns = {@JoinColumn(name = "certificates_id")}
    )
    @Audited
    private List<Certificate> certificates = new ArrayList<>();
}
