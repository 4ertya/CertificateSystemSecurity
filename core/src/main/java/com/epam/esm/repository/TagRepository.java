package com.epam.esm.repository;

import com.epam.esm.model.Tag;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public interface TagRepository {

    Tag findTagByName(String name);

    Tag createTag(Tag tag);

    List<Tag> findAllTags(int limit, int offset);

    Tag findTagById(long id);

    void updateTag(Tag tag);

    void deleteTag(Tag tag);

    long getCount();

    Tag getMostUsedTagOfUserWithHighestCostOfOrders();
}
