package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.expression.spel.ast.OpAnd;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {

    Optional<Tag> findTagByName(String name);

    @Query(value = "With user_with_biggest_cost AS (SELECT SUM(cost) AS orders_cost,\n" +
            "                                       user_id   AS ui\n" +
            "                                FROM orders\n" +
            "                                GROUP BY user_id\n" +
            "                                having sum(cost) >= ALL (Select SUM(cost) FROM orders GROUP BY user_id)\n" +
            "),\n" +
            "     \n" +
            "     tags_from_orders AS (SELECT tags.id   AS id,\n" +
            "                                 tags.name AS name,\n" +
            "                                 count(*)  AS count\n" +
            "                          FROM tags\n" +
            "                                   JOIN certificates_tags ON tags.id = certificates_tags.tags_id\n" +
            "                                   JOIN certificates ON certificates_tags.certificates_id = certificates.id\n" +
            "                                   JOIN ordered_certificates ON certificates.id = ordered_certificates.certificate_id\n" +
            "                                   JOIN orders ON ordered_certificates.order_id = orders.id\n" +
            "                                   JOIN user_with_biggest_cost on orders.user_id = user_with_biggest_cost.ui\n" +
            "                          GROUP BY (tags.id)\n" +
            "         )\n" +
            "\n" +
            "SELECT * from tags_from_orders\n" +
            "WHERE tags_from_orders.count >= (SELECT MAX(tags_from_orders.count) FROM tags_from_orders)\n",nativeQuery = true)
    List<Tag> getMostUsedTagOfUserWithHighestCostOfOrders();
}
