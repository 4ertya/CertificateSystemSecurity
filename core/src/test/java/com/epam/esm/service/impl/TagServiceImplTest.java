package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.validation.BasicValidator;
import com.epam.esm.validation.EntityValidator;
import com.epam.esm.validation.PaginationValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;
    @Mock
    private TagRepository tagRepository;
    @Spy
    private PaginationValidator paginationValidator;
    @Spy
    BasicValidator basicValidator;
    @Mock
    EntityValidator entityValidator;
    @Spy
    private final TagMapper mapper = new TagMapper(new ModelMapper());

    @Test
    void findAllTags() {
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("tag1");
        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("tag2");
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);

        TagDto tagDTO1 = new TagDto();
        tagDTO1.setId(1L);
        tagDTO1.setName("tag1");
        TagDto tagDTO2 = new TagDto();
        tagDTO2.setId(2L);
        tagDTO2.setName("tag2");
        List<TagDto> tagDTOs = new ArrayList<>();
        tagDTOs.add(tagDTO1);
        tagDTOs.add(tagDTO2);
        when(tagRepository.findAllTags(10, 0)).thenReturn(tags);
        Assertions.assertEquals(tagDTOs, tagService.findAllTags(new HashMap<>()));
        verify(paginationValidator).validatePaginationParams(any());
        verify(mapper, times(2)).toDTO(any());
    }

    @Test
    void findTagById() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tagName");
        when(tagRepository.findTagById(1)).thenReturn(tag);
        TagDto returnedTag = tagService.findTagById(1);
        assertEquals(tag.getName(), returnedTag.getName());
        verify(basicValidator).validateIdIsPositive(1);
    }

    @Test
    void findTagByIdUnsuccessful() {
        when(tagRepository.findTagById(1)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> tagService.findTagById(1));
        verify(basicValidator).validateIdIsPositive(1);
    }


    @Test
    void createTag() {
        String name = "newTag";
        Tag tag = new Tag();
        tag.setName(name);
        Tag tagReturned = new Tag();
        tagReturned.setName(tag.getName());
        tagReturned.setId(24L);
        when(tagRepository.createTag(tag)).thenReturn(tagReturned);
        TagDto tagDto = new TagDto();
        tagDto.setName(tag.getName());
        TagDto tagDTOReturned = tagService.createTag(tagDto);
        assertEquals(tagDTOReturned.getId(), tagReturned.getId());
        verify(entityValidator).validateTag(any());
    }

    @Test
    void deleteTag() {
        doNothing().when(tagRepository).deleteTag(new Tag());
        when(tagRepository.findTagById(3)).thenReturn(new Tag());
        tagService.deleteTag(3);
    }

}