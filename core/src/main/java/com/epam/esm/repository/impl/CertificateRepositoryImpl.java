package com.epam.esm.repository.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.specification.SearchSpecification;
import com.epam.esm.repository.specification.SortSpecification;
import com.epam.esm.repository.specification.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository

public class CertificateRepositoryImpl implements CertificateRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final static String ID = "id";

    @Override
    public List<Certificate> findAllCertificates(List<Specification> specifications, int limit, int offset) {
        return entityManager.createQuery(buildCriteriaQuery(specifications))
                .setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    @Override
    public Certificate findCertificateById(long id) {
        return entityManager.find(Certificate.class, id);
    }

    @Override
    public Certificate createCertificate(Certificate certificate) {
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public void deleteCertificate(Certificate certificate) {
        entityManager.remove(certificate);
    }

    @Override
    public long getCount(List<Specification> specifications) {
        CriteriaQuery<Certificate> criteriaQuery = buildCriteriaQuery(specifications);
        return entityManager.createQuery(criteriaQuery).getResultStream().count();
    }

    private CriteriaQuery<Certificate> buildCriteriaQuery(List<Specification> specifications) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(ID)));
        List<Predicate> predicateList = new ArrayList<>();
        specifications.forEach(specification -> {
            if (specification.getClass().getInterfaces()[0].getSimpleName().equals("SearchSpecification")) {
                predicateList.add(((SearchSpecification) specification).toPredicate(criteriaBuilder, root));
            } else {
                criteriaQuery.orderBy(((SortSpecification) specification).toOrder(criteriaBuilder, root));
            }
        });
        criteriaQuery.where(predicateList.toArray(new Predicate[0])).groupBy(root.get(ID));
        return criteriaQuery;
    }
}
