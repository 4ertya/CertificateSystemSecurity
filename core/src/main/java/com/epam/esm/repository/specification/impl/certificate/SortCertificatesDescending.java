package com.epam.esm.repository.specification.impl.certificate;


import com.epam.esm.repository.specification.SortSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public class SortCertificatesDescending implements SortSpecification {

    private final String field;

    public SortCertificatesDescending(String field) {
        this.field = field;
    }

    @Override
    public Order toOrder(CriteriaBuilder criteriaBuilder, Root root) {
        return criteriaBuilder.desc(root.get(field));
    }
}
