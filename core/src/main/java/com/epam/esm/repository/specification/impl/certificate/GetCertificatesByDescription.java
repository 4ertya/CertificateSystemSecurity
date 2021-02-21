package com.epam.esm.repository.specification.impl.certificate;

import com.epam.esm.repository.specification.SearchSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GetCertificatesByDescription implements SearchSpecification {

    private String description;

    public GetCertificatesByDescription(String description) {
        this.description = description;
    }

    @Override
    public Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root) {
        return criteriaBuilder.like(criteriaBuilder.lower(
                root.get("description")), "%" + description.toLowerCase() + "%");
    }
}
