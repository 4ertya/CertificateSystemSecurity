package com.epam.esm.repository.specification.impl.certificate;



import com.epam.esm.repository.specification.SearchSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GetCertificatesByName implements SearchSpecification {

    private final String name;

    public GetCertificatesByName(String name) {
        this.name = name;
    }

    @Override
    public Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root) {
        return criteriaBuilder.like(criteriaBuilder.lower(
                root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
