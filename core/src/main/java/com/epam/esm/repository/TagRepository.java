package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.expression.spel.ast.OpAnd;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {

    Optional<Tag> findTagByName(String name);

    @Query(value = "With user_with_biggest_cost AS (SELECT SUM(cost) AS orders_cost,\n" +
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
            "LIMIT 1;",nativeQuery = true)
    default Tag getMostUsedTagOfUserWithHighestCostOfOrders() {
        return null;
    }
}
