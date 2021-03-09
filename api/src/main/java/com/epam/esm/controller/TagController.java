package com.epam.esm.controller;


import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import com.epam.esm.util.HateoasBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;
    private final HateoasBuilder hateoasBuilder;

    @GetMapping
    public RepresentationModel findAllTags(@RequestParam Map<String, String> params) {
        List<TagDto> tags = tagService.findAllTags(params);
        long tagsCount = tagService.getCount();
        return hateoasBuilder.addLinksForListOfTagDTOs(tags, params, tagsCount);
    }

    @GetMapping("/{id}")
    public TagDto findTagById(@PathVariable("id") long id) {
        TagDto tagDto = tagService.findTagById(id);
        return hateoasBuilder.addLinksForTagDTO(tagDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TagDto createTag(@RequestBody TagDto tagDto) {
        TagDto tag = tagService.createTag(tagDto);
        return hateoasBuilder.addLinksForTagDTO(tag);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public TagDto updateTag(@PathVariable("id") long id, @RequestBody TagDto tagDto) {
        tagDto.setId(id);
        TagDto tag = tagService.updateTag(tagDto);
        return hateoasBuilder.addLinksForTagDTO(tag);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTag(@PathVariable("id") long id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/most-used")
    public TagDto findMostUsedTag() {
        TagDto tag = tagService.getMostUsedTagOfUserWithHighestCostOfOrders();
        return hateoasBuilder.addLinksForTagDTO(tag);
    }

}
