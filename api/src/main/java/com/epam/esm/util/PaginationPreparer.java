package com.epam.esm.util;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class PaginationPreparer {

    public List<Link> preparePaginationLinks(Object invocationValue, Map<String, String> params, long entityCount) {
        int firstPage=1;
        int currentPage = getPageNumber(params);
        long pageCount = calculatePageCount(params, entityCount);
        List<Link> links = new ArrayList<>();
        if (pageCount != firstPage && currentPage != firstPage) {
            params.replace(Constant.PAGE, String.valueOf(firstPage));
            links.add(linkTo(invocationValue).withRel(Constant.FIRST_PAGE));
        }
        if (currentPage != pageCount) {
            params.replace(Constant.PAGE, String.valueOf(currentPage + firstPage));
            links.add(linkTo(invocationValue).withRel(Constant.NEXT_PAGE));
        }
        if (currentPage != firstPage) {
            params.replace(Constant.PAGE, String.valueOf(currentPage - firstPage));
            links.add(linkTo(invocationValue).withRel(Constant.PREVIOUS_PAGE));
        }
        if (pageCount != firstPage && currentPage != pageCount) {
            params.replace(Constant.PAGE, String.valueOf(pageCount));
            links.add(linkTo(invocationValue).withRel(Constant.LAST_PAGE));
        }
        return links;
    }


    public Map<String, Long> preparePageInfo(Map<String, String> params, long entityCount) {
        int currentPage = getPageNumber(params);
        long pageCount = calculatePageCount(params, entityCount);
        Map<String, Long> page = new HashMap<>();
        page.put(Constant.PAGES_NUMBER, pageCount);
        page.put(Constant.CURRENT_PAGE, (long) currentPage);
        page.put(Constant.SIZE, Long.parseLong(params.get(Constant.SIZE)));
        page.put(Constant.TOTAL_NUMBER_OF_ELEMENTS, entityCount);
        return page;
    }

    private int getPageNumber(Map<String, String> params) {
        return Integer.parseInt(params.get(Constant.PAGE));
    }

    private long calculatePageCount(Map<String, String> params, long entityCount) {
        long pageCount = (long) Math.ceil(entityCount / Double.parseDouble(params.get(Constant.SIZE)));
        return pageCount == 0 ? 1 : pageCount;
    }

    private static class Constant {
        private final static String PAGE = "page";
        private final static String SIZE = "size";
        private final static String FIRST_PAGE = "first page";
        private final static String NEXT_PAGE = "next page";
        private final static String PREVIOUS_PAGE = "previous page";
        private final static String LAST_PAGE = "last page";
        private final static String PAGES_NUMBER = "totalPages";
        private final static String CURRENT_PAGE = "number";
        private final static String TOTAL_NUMBER_OF_ELEMENTS = "totalElements";
    }
}
