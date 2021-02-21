package com.epam.esm.repository.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final static String NAME = "name";
    private final static String SELECT_MOST_USED_TAG =
            "WITH user_with_biggest_cost AS (SELECT SUM(cost) AS orders_cost,\n" +
                    "                                        user_id   AS ui\n" +
                    "                                 FROM orders\n" +
                    "                                 GROUP BY user_id\n" +
                    "                                 ORDER BY orders_cost DESC\n" +
                    "                                 LIMIT 1)\n" +
                    "SELECT tags.id,\n" +
                    "       tags.name\n" +
                    "FROM tags\n" +
                    "         JOIN certificates_tags ON tags.id = certificates_tags.tags_id\n" +
                    "         JOIN certificates ON certificates_tags.certificates_id = certificates.id\n" +
                    "         JOIN orders_certificates ON certificates.id = orders_certificates.certificates_id\n" +
                    "         JOIN orders ON orders_certificates.orders_id = orders.id\n" +
                    "         JOIN user_with_biggest_cost on orders.user_id = user_with_biggest_cost.ui\n" +
                    "GROUP BY (tags.id)\n" +
                    "ORDER BY COUNT(tags.id) DESC\n" +
                    "LIMIT 1;";


    @Override
    public List<Tag> findAllTags(int limit, int offset) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        criteriaQuery.from(Tag.class);
        return entityManager.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset).getResultList();
    }

    @Override
    public Tag findTagByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get(NAME), name));
        return entityManager.createQuery(criteriaQuery).getResultStream().findFirst().orElse(null);
    }

    @Override
    public Tag findTagById(long id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public Tag createTag(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public void updateTag(Tag tag) {
        entityManager.merge(tag);
    }

    @Override
    public void deleteTag(Tag tag) {
        entityManager.remove(tag);
    }

    @Override
    public long getCount() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);
        count.select(criteriaBuilder.count(count.from(Tag.class)));
        return entityManager.createQuery(count).getSingleResult();
    }

    @Override
    public Tag getMostUsedTagOfUserWithHighestCostOfOrders() {
        return (Tag) entityManager.createNativeQuery(SELECT_MOST_USED_TAG, Tag.class).getSingleResult();
    }
}
