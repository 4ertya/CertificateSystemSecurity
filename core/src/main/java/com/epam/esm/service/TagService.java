package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;
import java.util.Map;

public interface TagService {
    TagDto createTag(TagDto tagDto);
    List<TagDto> findAllTags(Map<String, String> params);
    TagDto findTagById(long id);
    void deleteTag(long id);
    TagDto updateTag(TagDto tag);
    long getCount();
    TagDto getMostUsedTagOfUserWithHighestCostOfOrders();
}
