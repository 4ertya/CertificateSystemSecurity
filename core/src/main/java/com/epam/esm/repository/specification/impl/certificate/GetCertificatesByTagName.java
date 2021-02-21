package com.epam.esm.repository.specification.impl.certificate;



import com.epam.esm.repository.specification.SearchSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GetCertificatesByTagName implements SearchSpecification {

    private final String tagName;

    public GetCertificatesByTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root root) {
        return criteriaBuilder.like(criteriaBuilder.lower(
                root.join("tags").get("name")),
                "%" + tagName.toLowerCase() + "%");
    }
}
