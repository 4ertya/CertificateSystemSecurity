package com.epam.esm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Relation(itemRelation = "tag", collectionRelation = "tags")
public class TagDto extends RepresentationModel<TagDto> {
    private Long id;
    private String name;
}
