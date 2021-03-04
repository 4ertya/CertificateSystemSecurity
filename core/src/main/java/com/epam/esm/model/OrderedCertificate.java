package com.epam.esm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ordered_certificates")
@Data
@NoArgsConstructor
public class OrderedCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ordered_certificates_seq")
    @SequenceGenerator(name="ordered_certificates_seq",
            sequenceName="ordered_certificates_id_seq", allocationSize=1)
    private Long id;
    private Long certificateId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;

}
