package com.epam.esm.util;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class HateoasBuilder {
    private final PaginationPreparer paginationPreparer;

    public RepresentationModel<?> addLinksForListOfTagDTOs(List<TagDto> tags, Map<String, String> params, long tagsCount) {
        tags.forEach(tagDTO -> tagDTO.add(linkTo(methodOn(TagController.class)
                .findTagById(tagDTO.getId()))
                .withSelfRel()));

        Map<String, Long> page = paginationPreparer.preparePageInfo(params, tagsCount);
        List<Link> links = paginationPreparer.preparePaginationLinks(
                methodOn(TagController.class).findAllTags(params), params, tagsCount);
        CollectionModel<TagDto> collectionModel = CollectionModel.of(tags);
        return buildModel(collectionModel, links, page);
    }

    public TagDto addLinksForTagDTO(TagDto tagDTO) {
        tagDTO.add(linkTo(methodOn(TagController.class)
                .findTagById(tagDTO.getId()))
                .withSelfRel());
        tagDTO.add(createLinkToGetCertificates(Constant.TAG,
                tagDTO.getName(), Constant.CERTIFICATES));
        return tagDTO;
    }


    public RepresentationModel<?> addLinksForListOfCertificates(List<CertificateDto> certificates, Map<String, String> params, long certificatesCount) {
        certificates.forEach(certificate -> certificate.add(linkTo(methodOn(CertificateController.class)
                .findCertificateById(certificate.getId()))
                .withSelfRel()));
        Map<String, Long> page = paginationPreparer.preparePageInfo(params, certificatesCount);
        List<Link> links = paginationPreparer.preparePaginationLinks(
                methodOn(CertificateController.class).findAllCertificates(params),
                params, certificatesCount);
        CollectionModel<CertificateDto> collectionModel = CollectionModel.of(certificates);
        return buildModel(collectionModel, links, page);
    }

    public CertificateDto addLinksForCertificate(CertificateDto certificateDto) {
            certificateDto.getTags().forEach(tag -> tag.add(linkTo(methodOn(TagController.class)
                    .findTagById(tag.getId()))
                    .withSelfRel()));
        return certificateDto;
    }

    public RepresentationModel<?> addLinksForListOfUsers(List<UserDto> users, Map<String, String> params, long usersCount) {
        users.forEach(user -> user.add(linkTo(methodOn(UserController.class)
                .getUserById(user.getId()))
                .withSelfRel()));
        Map<String, Long> page = paginationPreparer.preparePageInfo(params, usersCount);
        List<Link> links = paginationPreparer.preparePaginationLinks(
                methodOn(UserController.class).getUsers(params), params, usersCount);
        CollectionModel<UserDto> collectionModel = CollectionModel.of(users);
        return buildModel(collectionModel, links, page);
    }

    public UserDto addLinksForUser(UserDto userDto) {
        userDto.add(linkTo(methodOn(UserController.class)
                .getUserById(userDto.getId()))
                .withSelfRel());
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, String.valueOf(userDto.getId()));
        userDto.add(linkTo(methodOn(OrderController.class)
                .getAllOrders(params))
                .withRel(Constant.ORDERS));
        return userDto;
    }

    public RepresentationModel<?> addLinksForListOfOrders(List<OrderDto> orders, Map<String, String> params, long ordersCount) {
        orders.forEach(order -> order.add(linkTo(methodOn(UserController.class)
                .getUserOrders(order.getUserId(),params))
                .withSelfRel()));
        Map<String, Long> page = paginationPreparer.preparePageInfo(params, ordersCount);
        List<Link> links = paginationPreparer.preparePaginationLinks(
                methodOn(OrderController.class).getAllOrders(params), params, ordersCount);
        CollectionModel<OrderDto> collectionModel = CollectionModel.of(orders);
        return buildModel(collectionModel, links, page);
    }

    public OrderDto addLinksForOrder(OrderDto orderDto) {
        orderDto.getCertificates().forEach(certificate -> certificate.add(linkTo(methodOn(CertificateController.class)
                .findCertificateById(certificate.getCertificateId()))
                .withSelfRel()));
        Map<String, String> params = new HashMap<>();
        orderDto.add(linkTo(methodOn(UserController.class)
                .getUserOrders(orderDto.getUserId(),new HashMap<>()))
                .withRel(Constant.USERS_ORDERS));
        return orderDto;
    }

    private Link createLinkToGetCertificates(String param, String value, String rel) {
        Map<String, String> params = new HashMap<>();
        params.put(param, value);
        return linkTo(methodOn(CertificateController.class)
                .findAllCertificates(params))
                .withRel(rel);
    }

    private RepresentationModel<?> buildModel(Object entity, Iterable<Link> links, Object embeddedEntity) {
        return HalModelBuilder
                .halModelOf(entity)
                .links(links)
                .embed(embeddedEntity, LinkRelation.of(Constant.PAGE))
                .build();
    }

    private static class Constant {
        private final static String PAGE = "page";
        private final static String TAG = "tag";
        private final static String CERTIFICATES = "certificates";
        private final static String USER_ID = "userId";
        private final static String ORDERS = "orders";
        private final static String USERS_ORDERS = "user's orders";
    }

}
