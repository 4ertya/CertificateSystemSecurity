package com.epam.esm.repository.specification;

import com.epam.esm.model.Certificate;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CertificateSpecification implements Specification<Certificate> {

    private final List<SearchCriteria> searchCriteria;
    private final Map<String, String> params;
    private final static List<String> field = new ArrayList<>();

    static {
        field.add("name");
        field.add("description");
        field.add("tag");
    }

    public CertificateSpecification(Map<String, String> params) {
        this.params = params;
        this.searchCriteria = new ArrayList<>();
        fillInSearchCriteria();
    }

    private void fillInSearchCriteria() {
        params.keySet().forEach(key -> {
            if (params.get(key) != null && field.contains(key)) {
                searchCriteria.add(new SearchCriteria(key, params.get(key)));
            }
        });
    }

    @Override
    public Predicate toPredicate(Root<Certificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        searchCriteria.forEach(criteria -> {
            if (!criteria.getKey().equals("tag")) {
                predicates.add(criteriaBuilder.like(
                        root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%"));
            } else {
                String[] tagNames = params.get("tag").split(",");
                for (String tagName : tagNames) {
                    predicates.add(criteriaBuilder.like(
                            root.join("tags")
                                    .get("name"), "%" + tagName.trim() + "%"));
                }
                query.groupBy(root.get("id"));
            }
        });

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
