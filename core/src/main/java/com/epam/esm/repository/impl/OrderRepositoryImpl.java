package com.epam.esm.repository.impl;

import com.epam.esm.model.Order;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.specification.SearchSpecification;
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
public class OrderRepositoryImpl implements OrderRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Order addOrder(Order order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public List<Order> getOrders(List<SearchSpecification> specifications, int limit, int offset) {
        return entityManager.createQuery(buildCriteriaQuery(specifications))
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    private CriteriaQuery<Order> buildCriteriaQuery(List<SearchSpecification> specifications) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        List<Predicate> predicateList = new ArrayList<>();
        specifications.forEach(specification -> predicateList.add(specification.toPredicate(criteriaBuilder, root)));
        criteriaQuery.where(predicateList.toArray(new Predicate[0]));
        return criteriaQuery;
    }

    @Override
    public Order getOrderById(long id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public long getCount(List<SearchSpecification> specifications) {
        CriteriaQuery<Order> criteriaQuery = buildCriteriaQuery(specifications);
        return entityManager.createQuery(criteriaQuery).getResultStream().count();
    }
}
